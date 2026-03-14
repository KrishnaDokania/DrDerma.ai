import os
from icrawler.builtin import BingImageCrawler

BASE_DIR = "data/images"

TARGET_IMAGES = 70

for disease in os.listdir(BASE_DIR):

    if disease.startswith("."):
        continue

    folder = os.path.join(BASE_DIR, disease)

    if not os.path.isdir(folder):
        continue

    existing_images = len([
        f for f in os.listdir(folder)
        if f.lower().endswith((".jpg", ".jpeg", ".png"))
    ])

    print(f"\n{disease} → {existing_images} images found")

    if existing_images >= TARGET_IMAGES:
        print("Skipping (already enough images)")
        continue

    search_query = disease.replace("_", " ")

    print(f"Downloading images for: {search_query}")

    crawler = BingImageCrawler(storage={"root_dir": folder})

    crawler.crawl(
        keyword=search_query + " skin disease",
        max_num=TARGET_IMAGES - existing_images
    )

print("\nDownload process finished.")