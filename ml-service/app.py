from fastapi import FastAPI, UploadFile, File
from fastapi.responses import JSONResponse
import torch
from torchvision import transforms
from PIL import Image
import io
import json
import numpy as np
import os
import torch.nn.functional as F

from models.embedding_backbone import EfficientNetEmbedding

# =========================
# App Config
# =========================
app = FastAPI(
    title="DrDerma ML Service",
    description="Skin disease triage embedding service",
    version="1.0"
)

DEVICE = torch.device("cpu")

# =========================
# Paths
# =========================
BASE_DIR = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

MODEL_PATH = os.path.join(
    os.path.dirname(__file__),
    "models",
    "skin_gate_model.pt"
)

CENTROID_PATH = os.path.join(
    BASE_DIR,
    "data",
    "embeddings",
    "centroids.json"
)

# =========================
# Preprocessing
# =========================
transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(
        mean=[0.485, 0.456, 0.406],
        std=[0.229, 0.224, 0.225]
    )
])

# =========================
# Load Models
# =========================
@app.on_event("startup")
def load_model():
    global skin_model
    global embed_model
    global centroids

    torch.set_num_threads(2)

    # Load Skin Detection Model
    skin_model = torch.jit.load(MODEL_PATH, map_location=DEVICE)
    skin_model.eval()

    # Load Embedding Model
    embed_model = EfficientNetEmbedding().to(DEVICE)
    embed_model.eval()

    # Load Disease Centroids
    with open(CENTROID_PATH, "r") as f:
        centroids = json.load(f)

    print("Models and centroids loaded successfully.")

# =========================
# Health Endpoint
# =========================
@app.get("/health")
def health():
    return {"status": "ok"}

# =========================
# Validate Image
# =========================
def validate_image(file: UploadFile):

    if file.content_type is None:
        return False

    if not file.content_type.startswith("image"):
        return False

    return True


# =========================
# Skin Check Endpoint
# =========================
@app.post("/skin-check")
async def skin_check(file: UploadFile = File(...)):

    if not validate_image(file):
        return JSONResponse(
            status_code=400,
            content={"error": "Invalid image file"}
        )

    try:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception:
        return JSONResponse(
            status_code=400,
            content={"error": "Invalid image file"}
        )

    image_tensor = transform(image).unsqueeze(0).to(DEVICE)

    with torch.inference_mode():
        outputs = skin_model(image_tensor)
        probs = torch.softmax(outputs, dim=1)
        skin_prob = probs[0][1].item()

    threshold = 0.5

    return {
        "is_skin": skin_prob > threshold,
        "confidence": round(skin_prob, 4)
    }

# =========================
# Embedding Endpoint
# =========================
@app.post("/embed")
async def embed(file: UploadFile = File(...)):

    if not validate_image(file):
        return JSONResponse(
            status_code=400,
            content={"error": "Only JPG and PNG images allowed"}
        )

    try:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception:
        return JSONResponse(
            status_code=400,
            content={"error": "Invalid image file"}
        )

    image_tensor = transform(image).unsqueeze(0).to(DEVICE)

    with torch.inference_mode():
        features = embed_model(image_tensor)
        features = F.normalize(features, p=2, dim=1)

    vector = features.squeeze().cpu().tolist()

    return {
        "vector": vector
    }

# =========================
# Similarity Check Endpoint
# =========================
@app.post("/similarity-check")
async def similarity_check(file: UploadFile = File(...)):

    if not validate_image(file):
        return JSONResponse(
            status_code=400,
            content={"error": "Only JPG and PNG images allowed"}
        )

    try:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception:
        return JSONResponse(
            status_code=400,
            content={"error": "Invalid image file"}
        )

    image_tensor = transform(image).unsqueeze(0).to(DEVICE)

    with torch.inference_mode():
        features = embed_model(image_tensor)
        features = F.normalize(features, p=2, dim=1)

    query_vector = features.squeeze().cpu().numpy()

    results = []

    for item in centroids:

        centroid_vector = np.array(item["centroid"])
        cosine_similarity = float(np.dot(query_vector, centroid_vector))

        results.append({
            "disease": item["disease"],
            "similarity": round(cosine_similarity, 4)
        })

    results.sort(key=lambda x: x["similarity"], reverse=True)

    return {
        "rankedResults": results
    }