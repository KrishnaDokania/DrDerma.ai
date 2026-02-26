import os
import json
import torch
import torch.nn.functional as F
from PIL import Image
from torchvision import transforms

from models.embedding_backbone import EfficientNetEmbedding


# ==========================
# CONFIG
# ==========================

DATASET_DIR = r"C:\Users\ilfan\Downloads\DrDerma.Ai\data\images"
OUTPUT_FILE = r"C:\Users\ilfan\Downloads\DrDerma.Ai\data\embeddings\embeddings.json"

DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")


# ==========================
# TRANSFORM (MUST MATCH FastAPI)
# ==========================

transform = transforms.Compose([
    transforms.Resize((224, 224)),
    transforms.ToTensor(),
    transforms.Normalize(
        mean=[0.485, 0.456, 0.406],
        std=[0.229, 0.224, 0.225]
    )
])


# ==========================
# LOAD EMBEDDING MODEL
# ==========================

model = EfficientNetEmbedding().to(DEVICE)
model.eval()

print("Embedding model loaded.")


# ==========================
# GENERATE EMBEDDINGS
# ==========================

all_embeddings = []

for disease in os.listdir(DATASET_DIR):
    disease_dir = os.path.join(DATASET_DIR, disease)

    if not os.path.isdir(disease_dir):
        continue

    for img_name in os.listdir(disease_dir):
        img_path = os.path.join(disease_dir, img_name)

        try:
            image = Image.open(img_path).convert("RGB")

            image_tensor = transform(image).unsqueeze(0).to(DEVICE)

            with torch.inference_mode():
                features = model(image_tensor)

                # 🔥 L2 NORMALIZATION (CRITICAL)
                features = F.normalize(features, p=2, dim=1)

            vector = features.squeeze().cpu().tolist()

            all_embeddings.append({
                "disease": disease,
                 "image": img_name,
                "vector": vector
             })

        except Exception as e:
            print(f"Skipping {img_path}: {e}")
            # ==========================
# COMPUTE DISEASE CENTROIDS
# ==========================

from collections import defaultdict
import numpy as np

disease_vectors = defaultdict(list)

for item in all_embeddings:
    disease_vectors[item["disease"]].append(item["vector"])

centroids = []

for disease, vectors in disease_vectors.items():
    vectors_np = np.array(vectors)

    # Mean of normalized vectors
    centroid = np.mean(vectors_np, axis=0)

    # Re-normalize centroid
    centroid = centroid / np.linalg.norm(centroid)

    centroids.append({
        "disease": disease,
        "centroid": centroid.tolist()
    })

print("Generated centroids:", len(centroids))



# ==========================
# SAVE TO FILE
# ==========================
with open(OUTPUT_FILE, "w") as f:
    json.dump(all_embeddings, f, indent=2)

# Save centroids
CENTROID_FILE = OUTPUT_FILE.replace("embeddings.json", "centroids.json")

with open(CENTROID_FILE, "w") as f:
    json.dump(centroids, f, indent=2)

print("Generated embeddings:", len(all_embeddings))
print("Centroids saved.")
