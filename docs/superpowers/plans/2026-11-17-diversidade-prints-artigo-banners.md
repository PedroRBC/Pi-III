# Diversidade de Prints para Artigo e Banners Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Criar uma prancha editorial e três banners novos, exportados em PNG e acompanhados de HTML/CSS editável.

**Architecture:** Um único gerador HTML/CSS em `assets/editorial/` reutiliza as capturas existentes e define quatro canvases com dimensões fixas. Um script Playwright ou Chromium headless captura cada canvas em PNG; um verificador Python confirma dimensões.

**Tech Stack:** HTML, CSS, imagens PNG existentes, Chromium headless disponível no ambiente, Python/Pillow para verificação.

**Spec:** `docs/superpowers/specs/2026-11-17-diversidade-prints-artigo-banners-design.md`

## Global Constraints

- Usar somente capturas e identidade visual já presentes no projeto.
- Manter o conteúdo crítico dentro da área segura central dos banners.
- No máximo duas famílias tipográficas e um CTA por peça.
- Contraste suficiente para títulos e texto de apoio.
- Entregar dimensões exatas em PNG e a fonte editável em HTML/CSS.

## Review Focus

- Captura ausente ou caminho inválido: o gerador deve falhar claramente antes da exportação.
- Dimensão incorreta: cada PNG deve corresponder exatamente ao canvas declarado.
- Texto fora da área segura: revisar visualmente as quatro peças em escala real.
- Contraste insuficiente em estados escuros: revisar títulos e CTA.
- Exportação sem suporte a fontes remotas: usar fontes de sistema e verificar renderização local.

### Task 1: Montar layouts editoriais

**Files:**
- Create: `assets/editorial/index.html`
- Create: `assets/editorial/styles.css`

- [ ] **Step 1:** Criar quatro canvases nomeados `article`, `banner-wide`, `banner-square` e `banner-story`, com dimensões exatas e composição híbrida definida na spec.
- [ ] **Step 2:** Referenciar as capturas existentes, mantendo os caminhos relativos válidos e textos em português.
- [ ] **Step 3:** Aplicar a identidade de azulejos, roxo da marca, estados e safe zones sem dependências externas.

### Task 2: Exportar PNGs

**Files:**
- Create: `assets/editorial/export.py`
- Create: `assets/editorial/article-1600x900.png`
- Create: `assets/banners/detector-de-gases/banner-wide-1500x500.png`
- Create: `assets/banners/detector-de-gases/banner-square-1080x1080.png`
- Create: `assets/banners/detector-de-gases/banner-story-1080x1920.png`

- [ ] **Step 1:** Implementar captura headless dos quatro elementos pelo HTML local.
- [ ] **Step 2:** Exportar os quatro PNGs nos caminhos definidos.

### Task 3: Verificar artefatos

**Files:**
- Create: `assets/editorial/verify_exports.py`

- [ ] **Step 1:** Verificar existência e dimensões exatas dos quatro PNGs.
- [ ] **Step 2:** Rodar o verificador e inspecionar a prancha de contato para confirmar legibilidade.
