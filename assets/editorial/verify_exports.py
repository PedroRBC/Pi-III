from pathlib import Path
from PIL import Image

ROOT = Path(__file__).resolve().parents[2]
EXPECTED = {
    ROOT / "assets/editorial/article-1600x900.png": (1600, 900),
    ROOT / "assets/banners/detector-de-gases/banner-wide-1500x500.png": (1500, 500),
    ROOT / "assets/banners/detector-de-gases/banner-square-1080x1080.png": (1080, 1080),
    ROOT / "assets/banners/detector-de-gases/banner-story-1080x1920.png": (1080, 1920),
}

for path, size in EXPECTED.items():
    if not path.exists():
        raise SystemExit(f"missing export: {path}")
    actual = Image.open(path).size
    if actual != size:
        raise SystemExit(f"wrong size: {path} expected {size}, got {actual}")
print(f"verified {len(EXPECTED)} exports")
