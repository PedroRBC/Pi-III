from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[2]
SCREEN = ROOT / "app-android" / "screenshots"
OUT = ROOT / "assets"

FONT = "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf"
BOLD = "/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf"

def font(path, size):
    return ImageFont.truetype(path, size)

def screen(name, width, height):
    im = Image.open(SCREEN / name).convert("RGB")
    ratio = min(width / im.width, height / im.height)
    return im.resize((int(im.width * ratio), int(im.height * ratio)), Image.Resampling.LANCZOS)

def rounded_paste(base, im, box, radius=26, border=10, border_color="#19191d", angle=0):
    im = im.rotate(angle, expand=True, resample=Image.Resampling.BICUBIC)
    frame = Image.new("RGB", (im.width + border * 2, im.height + border * 2), border_color)
    mask = Image.new("L", frame.size, 0)
    ImageDraw.Draw(mask).rounded_rectangle((0, 0, frame.width - 1, frame.height - 1), radius=radius, fill=255)
    frame.paste(im, (border, border))
    frame.putalpha(mask)
    base.paste(frame.convert("RGB"), box, frame)

def draw_article():
    im = Image.new("RGB", (1600, 900), "#f4f1eb")
    d = ImageDraw.Draw(im)
    d.polygon([(0, 0), (820, 0), (620, 900), (0, 900)], fill="#eee8dc")
    d.text((90, 88), "PROJETO INTEGRADOR III · ANDROID", fill="#5b3d9e", font=font(BOLD, 18))
    d.text((90, 155), "Um olhar claro", fill="#24212d", font=font(BOLD, 78))
    d.text((90, 240), "sobre o ar da", fill="#5b3d9e", font=font(BOLD, 78))
    d.text((90, 320), "casa.", fill="#5b3d9e", font=font(BOLD, 78))
    d.multiline_text((90, 445), "O Detector de Gases transforma leituras\ndo Arduino em estados que qualquer pessoa\nentende: normal, atenção e perigo.", fill="#5d5862", font=font(FONT, 25), spacing=10)
    d.rectangle((90, 610, 290, 618), fill="#5b3d9e")
    d.text((90, 640), "Conexão Bluetooth · alertas · histórico da sessão", fill="#6a6570", font=font(FONT, 18))
    rounded_paste(im, screen("10_tema_escuro_normal.png", 300, 680), (735, 45), angle=11)
    rounded_paste(im, screen("03_principal_atencao.png", 315, 700), (910, 55), angle=-1)
    rounded_paste(im, screen("04_principal_perigo.png", 330, 720), (1115, 100), angle=-11)
    d.ellipse((1390, 88, 1440, 138), outline="#5b3d9e", width=2)
    d.text((1406, 100), "01", fill="#5b3d9e", font=font(BOLD, 16))
    d.text((1460, 94), "ESTADO\nEM UM OLHAR", fill="#5b3d9e", font=font(BOLD, 14), spacing=2)
    im.save(OUT / "editorial" / "article-1600x900.png")

def draw_wide():
    im = Image.new("RGB", (1500, 500), "#2b1d50")
    d = ImageDraw.Draw(im)
    for x in range(-100, 1600, 56): d.line((x, 0, x + 180, 500), fill="#dcd2f220", width=1)
    for y in range(0, 501, 56): d.line((0, y, 1500, y), fill="#dcd2f220", width=1)
    d.text((80, 65), "DETECTOR DE GASES", fill="#dcd2f2", font=font(BOLD, 18))
    d.text((80, 125), "Quando o ar muda,", fill="white", font=font(BOLD, 65))
    d.text((80, 195), "você sabe.", fill="#dcd2f2", font=font(BOLD, 65))
    d.text((80, 300), "Alertas claros para a cozinha de casa.", fill="#dcd2f2", font=font(FONT, 24))
    d.rounded_rectangle((80, 355, 300, 410), radius=5, fill="#5b3d9e")
    d.text((108, 370), "Conheça o app", fill="white", font=font(BOLD, 20))
    rounded_paste(im, screen("04_principal_perigo.png", 320, 650), (1035, 30), angle=-9)
    d.rounded_rectangle((1150, 56, 1415, 98), radius=21, outline="#dcd2f2", width=1)
    d.text((1170, 68), "Bluetooth · Arduino · Android", fill="#dcd2f2", font=font(FONT, 15))
    im.save(OUT / "banners" / "detector-de-gases" / "banner-wide-1500x500.png")

def draw_square():
    im = Image.new("RGB", (1080, 1080), "#f5f7f2")
    d = ImageDraw.Draw(im)
    d.ellipse((450, -220, 1230, 560), outline="#cfe1d2", width=2)
    d.ellipse((560, -110, 1120, 450), outline="#a9d3b8", width=2)
    d.text((86, 86), "DETECTOR DE GASES", fill="#2e7d4f", font=font(BOLD, 18))
    d.text((86, 160), "Ar normal.", fill="#24212d", font=font(BOLD, 84))
    d.text((86, 250), "Casa tranquila.", fill="#2e7d4f", font=font(BOLD, 84))
    d.multiline_text((86, 385), "Veja o estado do ambiente\nem poucos segundos.", fill="#59615b", font=font(FONT, 26), spacing=10)
    rounded_paste(im, screen("02_principal_normal.png", 350, 700), (655, 445), angle=-7)
    d.line((86, 1000, 994, 1000), fill="#2e7d4f", width=2)
    d.text((86, 1018), "01", fill="#2e7d4f", font=font(BOLD, 17))
    d.text((170, 1018), "MONITORAMENTO DOMÉSTICO", fill="#657069", font=font(FONT, 17))
    d.text((960, 1012), "→", fill="#2e7d4f", font=font(BOLD, 26))
    im.save(OUT / "banners" / "detector-de-gases" / "banner-square-1080x1080.png")

def draw_story():
    im = Image.new("RGB", (1080, 1920), "#f4f1eb")
    d = ImageDraw.Draw(im)
    d.ellipse((580, 330, 1530, 1280), fill="#e8e0f5")
    d.text((72, 76), "DETECTOR DE GASES", fill="#5b3d9e", font=font(BOLD, 18))
    d.text((870, 76), "03 / 03", fill="#5b3d9e", font=font(BOLD, 18))
    d.text((72, 260), "Do normal", fill="#24212d", font=font(BOLD, 92))
    d.text((72, 345), "ao perigo.", fill="#5b3d9e", font=font(BOLD, 92))
    d.text((72, 475), "Uma leitura que acompanha a casa.", fill="#6b6671", font=font(FONT, 26))
    rounded_paste(im, screen("02_principal_normal.png", 350, 610), (105, 700), angle=10)
    rounded_paste(im, screen("03_principal_atencao.png", 350, 610), (335, 930), angle=-5)
    rounded_paste(im, screen("04_principal_perigo.png", 350, 610), (465, 1195), angle=-12)
    d.line((72, 1770, 1008, 1770), fill="#5b3d9e", width=2)
    d.text((72, 1792), "Alertas no celular", fill="#5b3d9e", font=font(FONT, 24))
    d.text((930, 1782), "↗", fill="#5b3d9e", font=font(BOLD, 34))
    im.save(OUT / "banners" / "detector-de-gases" / "banner-story-1080x1920.png")

if __name__ == "__main__":
    (OUT / "editorial").mkdir(parents=True, exist_ok=True)
    (OUT / "banners" / "detector-de-gases").mkdir(parents=True, exist_ok=True)
    draw_article(); draw_wide(); draw_square(); draw_story()
    print("exported 4 PNGs")
