import os
from icrawler.builtin import BingImageCrawler

BASE_DIR = "data/images"

diseases = {
    "acne_vulgaris": "acne vulgaris skin disease",
    "cystic_acne": "cystic acne skin",
    "tinea_corporis": "tinea corporis ringworm skin",
    "tinea_pedis": "tinea pedis athlete foot skin",
    "tinea_capitis": "tinea capitis scalp fungal infection",
    "psoriasis_plaque": "plaque psoriasis skin",
    "guttate_psoriasis": "guttate psoriasis skin",
    "atopic_dermatitis": "atopic dermatitis eczema skin",
    "contact_dermatitis": "contact dermatitis skin rash",
    "seborrheic_dermatitis": "seborrheic dermatitis skin",
    "rosacea": "rosacea facial skin",
    "vitiligo": "vitiligo skin depigmentation",
    "melasma": "melasma facial pigmentation skin",
    "urticaria": "urticaria hives skin rash",
    "impetigo": "impetigo skin infection",
    "scabies": "scabies skin rash",
    "common_warts": "common warts skin",
    "herpes_simplex": "herpes simplex skin lesion",
    "chickenpox": "chickenpox skin rash"
}

os.makedirs(BASE_DIR, exist_ok=True)

for disease, query in diseases.items():

    folder = os.path.join(BASE_DIR, disease)

    os.makedirs(folder, exist_ok=True)

    print(f"\nDownloading images for {disease}")

    crawler = BingImageCrawler(
        storage={"root_dir": folder}
    )

    crawler.crawl(
        keyword=query,
        max_num=80
    )

print("\nDataset download completed.")