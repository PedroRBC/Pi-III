---
name: Detector de Gases
description: Parede de azulejos atrás do fogão; cada informação no seu ladrilho, o estado do ar no ladrilho grande.
colors:
  marca: "#5B3D9E"
  marca_base: "#45307A"
  marca_clara: "#DCD2F2"
  acao: "#5B3D9E"
  toque: "#295B3D9E"
  normal: "#2E7D4F"
  normal_vidrado: "#A9D3B8"
  normal_base: "#88BD9C"
  atencao: "#8F5F00"
  atencao_vidrado: "#E0A33A"
  atencao_base: "#B8822A"
  perigo: "#B3261E"
  perigo_base: "#8C1D17"
  sem_conexao: "#80868B"
  faixa_atencao: "#F6E6C4"
  faixa_perigo: "#F2D2CE"
  rejunte: "#D8D9DD"
  azulejo: "#FBFBFC"
  esmalte_base: "#E4E5E9"
  tinta: "#22262B"
  tinta_media: "#545A60"
  tinta_fraca: "#666C72"
  fio: "#E2E3E7"
  branco: "#FFFFFF"
typography:
  display:
    fontFamily: "Roboto (sans-serif)"
    fontSize: "40sp"
    fontWeight: 700
    letterSpacing: "-0.01em"
  headline:
    fontFamily: "Roboto Medium (sans-serif-medium)"
    fontSize: "32sp"
    fontWeight: 500
    fontFeature: "tnum"
  title-large:
    fontFamily: "Roboto Medium (sans-serif-medium)"
    fontSize: "22sp"
    fontWeight: 500
  title:
    fontFamily: "Roboto Medium (sans-serif-medium)"
    fontSize: "16sp"
    fontWeight: 500
  body-large:
    fontFamily: "Roboto (sans-serif)"
    fontSize: "16sp"
    fontWeight: 400
    lineHeight: "+3sp"
  body:
    fontFamily: "Roboto (sans-serif)"
    fontSize: "14sp"
    fontWeight: 400
    lineHeight: "+2sp"
  label:
    fontFamily: "Roboto (sans-serif)"
    fontSize: "12sp"
    fontWeight: 400
    fontFeature: "tnum"
rounded:
  barra: "2dp"
  ladrilho: "3dp"
spacing:
  junta: "4dp"
  margem-tela: "8dp"
  ladrilho: "16dp"
  cabecalho: "20dp"
  estado: "24dp"
components:
  ladrilho:
    backgroundColor: "{colors.azulejo}"
    rounded: "{rounded.ladrilho}"
    padding: "16dp"
  ladrilho-marca:
    backgroundColor: "{colors.marca}"
    textColor: "{colors.branco}"
    rounded: "{rounded.ladrilho}"
    padding: "16dp 16dp 18dp 20dp"
    height: "96dp"
  ladrilho-estado:
    backgroundColor: "{colors.azulejo}"
    textColor: "{colors.tinta}"
    typography: "{typography.display}"
    rounded: "{rounded.ladrilho}"
    padding: "24dp 24dp 22dp"
    height: "248dp"
  ladrilho-estado-perigo:
    backgroundColor: "{colors.perigo}"
    textColor: "{colors.branco}"
    typography: "{typography.display}"
    rounded: "{rounded.ladrilho}"
    padding: "24dp 24dp 22dp"
  ladrilho-sensor:
    backgroundColor: "{colors.azulejo}"
    textColor: "{colors.tinta}"
    typography: "{typography.headline}"
    rounded: "{rounded.ladrilho}"
    padding: "16dp"
    height: "168dp"
  botao-marca:
    backgroundColor: "{colors.marca}"
    textColor: "{colors.branco}"
    typography: "{typography.body-large}"
    rounded: "{rounded.ladrilho}"
    padding: "0 28dp"
    height: "52dp"
  ladrilho-botao:
    backgroundColor: "{colors.azulejo}"
    textColor: "{colors.acao}"
    typography: "{typography.body-large}"
    rounded: "{rounded.ladrilho}"
    height: "64dp"
  barra-estados:
    backgroundColor: "{colors.rejunte}"
    rounded: "{rounded.barra}"
    height: "36dp"
  item-detector:
    textColor: "{colors.tinta}"
    typography: "{typography.title}"
    padding: "10dp 24dp"
    height: "64dp"
---

# Design System: Detector de Gases

## Overview

**Creative North Star: "A Parede de Azulejos atrás do Fogão"**

A tela é um pedaço de parede de cozinha. O fundo é rejunte cinza-claro; cada bloco de informação é um ladrilho branco vitrificado assentado com juntas finas e iguais; um ladrilho de cabeçalho é pintado na cor da marca e um ladrilho decorativo carrega um motivo geométrico na cor da marca, desenhado em vetor. Nada flutua, nada é cartão solto: tudo está assentado na mesma parede, alinhado pela mesma junta.

A densidade é de consulta calma. O ladrilho de estado domina a primeira dobra e diz, com ícone, palavra e frase, o que está acontecendo e o que fazer; o resto (sensores, barra da última hora, resumo, gráficos) é leitura ocasional em ladrilhos menores. A cor de estado é contida: aparece como borda do ladrilho de estado e como vidrado nos ladrilhos da barra. Só o perigo toma o ladrilho inteiro em vermelho. À noite a parede muda de material (rejunte grafite, ladrilho cinza-esmalte) e a marca permanece.

O sistema recusa o painel escuro de casa inteligente com medidores e o app genérico de cartões soltos com sombra. Movimento é quase nenhum: um único crossfade curto no ladrilho de estado.

**Key Characteristics:**
- Rejunte como fundo, ladrilhos vitrificados como superfícies, juntas de 4 dp entre tudo.
- Profundidade só pelo filete de esmalte na base de cada ladrilho; sem sombras de elevação.
- Uma cor de marca por tema (roxo por padrão); cores de estado reservadas a estado.
- Estado sempre com ícone e palavra, nunca só cor.
- Roboto com números tabulares; tinta grafite em três intensidades.

## Colors

Paleta de cozinha azulejada: neutros cinza de rejunte e louça, uma cor de marca que o usuário escolhe no seletor de temas e uma família de três tons por estado (tinta, vidrado, base). Todo token tem par noturno em `values-night/colors.xml` com o mesmo nome; os valores noturnos ficam no sidecar.

### Primary
- **Roxo da Marca** (`marca`): o ladrilho de cabeçalho, o botão Conectar, o motivo do ladrilho pintado, o fundo do ícone do app e a cor da notificação fixa. No padrão é roxo; à noite fica praticamente igual, como âncora que atravessa claro e escuro. O ícone do app fica sempre roxo, porque o launcher não segue o tema.
- **Base da Marca** (`marca_base`): o filete de esmalte sob o ladrilho da marca.
- **Marca Lavada** (`marca_clara`): texto secundário sobre a marca (a linha de conexão).
- **Ação** (`acao`): texto de botões em ladrilho branco, traço e ponto dos gráficos, borda do estado "Conectado, aguardando". De dia é a própria marca; à noite clareia para manter contraste sobre o ladrilho escuro. `toque` é a mesma cor translúcida usada no ripple.

### Temas
O seletor (botão de paleta no cabeçalho) troca só os cinco tokens da marca: `marca`, `marca_base`, `marca_clara`, `acao` e `toque`. São atributos de tema (`?attr/marca`); cada tema aponta para cores `<tema>`, `<tema>_base`, `<tema>_claro`, `<tema>_acao` e `<tema>_toque`, que têm par noturno. Neutros e cores de estado são os mesmos em todos.
- **Roxo e cinza** (padrão): `#5B3D9E`.
- **Azul azulejo**: `#1E4FA3`, o cobalto original.
- **Petróleo**: `#0E5A6B`.
- **Grafite**: `#3B4046`.

A escolha fica salva no aparelho e a tela é recriada ao trocar. No diálogo, cada opção mostra o ladrilho do cabeçalho pintado no próprio tema, e o fundo do diálogo é a louça (`azulejo`), para a amostra do Grafite aparecer no escuro.

### Secondary (cores de estado)
Cada estado tem três papéis: a cor de **tinta/borda** (legível como texto ou traço), o **vidrado** (face de ladrilho pintado na barra) e a **base** (filete de esmalte desse ladrilho).
- **Verde Normal** (`normal`, `normal_vidrado`, `normal_base`): borda e ícone do estado "Ar normal"; ladrilhos normais da barra.
- **Âmbar Atenção** (`atencao`, `atencao_vidrado`, `atencao_base`): o vidrado âmbar é a borda do ladrilho de estado e a cor da notificação; a tinta âmbar escura pinta o ícone de atenção, que precisa de contraste sobre o branco.
- **Vermelho Perigo** (`perigo`, `perigo_base`): o único estado que vira ladrilho cheio, com texto e ícone brancos; também é a face dos ladrilhos de perigo na barra.
- **Cinza Sem Conexão** (`sem_conexao`): borda e ícone de "Desconectado", "Sem conexão" e "Sem leituras". Neutro de propósito: ausência de dado nunca se parece com normal.
- **Faixas de gráfico** (`faixa_atencao`, `faixa_perigo`): fundos pálidos que marcam no gráfico quando o estado foi atenção ou perigo.

### Neutral
- **Rejunte** (`rejunte`): fundo da janela e a massa entre os ladrilhos da barra.
- **Louça** (`azulejo`): face de todo ladrilho comum.
- **Esmalte de Base** (`esmalte_base`): o filete de 2 dp sob cada ladrilho comum.
- **Tinta Grafite** (`tinta`): títulos, valores, palavra de estado.
- **Tinta Média** (`tinta_media`): frases de corpo e mensagem do estado.
- **Tinta Fraca** (`tinta_fraca`): nomes técnicos (MQ-2), rótulos de eixo, horários, valores de leitura antiga.
- **Fio** (`fio`): divisórias da tabela e linhas de base dos gráficos.

### Named Rules
**The Cor Só no Estado Rule.** Verde, âmbar e vermelho aparecem só onde há estado: borda e ícone do ladrilho de estado, ladrilhos da barra, faixas do gráfico e notificações. Nunca como decoração, nunca em títulos ou botões.

**The Uma Marca Rule.** Cada tema tem uma única cor de marca, que vale no claro e no escuro. Não se introduz segunda cor de destaque, e nenhum tema usa verde, âmbar ou vermelho.

## Typography

**Display Font:** Roboto (fonte do sistema, `sans-serif`)
**Body Font:** Roboto (`sans-serif`, `sans-serif-medium` para ênfase)

**Character:** Uma família só, a da plataforma, usada com sobriedade: negrito apenas na palavra de estado, médio para títulos e números, regular para o resto. Todo número que muda (valores, horários, porcentagens, tabela, eixos) usa algarismos tabulares (`tnum`), como inscrição em placa.

### Hierarchy
- **Display** (bold, 40sp, espaçamento -0.01): a palavra de estado no ladrilho grande ("Ar normal", "Atenção", "Perigo"). Uso único na tela.
- **Headline** (medium, 32sp, tabular): o valor de cada sensor, com a unidade ao lado em rótulo de 14sp.
- **Title Large** (medium, 22sp): o nome "Detector de Gases" no ladrilho da marca.
- **Title** (medium, 16sp, `Texto.Titulo`): nome da casa de cada sensor e título de cada ladrilho de seção ("Como esteve o ar", "Resumo da sessão"); nome do detector no seletor.
- **Body Large** (regular, 16sp, +3sp entre linhas): mensagem do que fazer no ladrilho de estado, largura máxima de 320 dp; rótulo dos botões (médio).
- **Body** (regular, 14sp, +2sp, `Texto.Corpo`): frases de significado, porcentagens por estado, células da tabela.
- **Label** (regular, 12sp, `Texto.Tecnico`): nomes técnicos, rótulos de eixo e cabeçalhos da tabela; sobe para 13sp em horários e contagens ao lado de títulos.

### Named Rules
**The Nome da Casa Primeiro Rule.** O nome em linguagem de casa ("Gás e fumaça") vai em Title e tinta cheia; o nome técnico ("MQ-2") vem logo depois em Label e tinta fraca. Nunca o contrário.

**The Número Tabular Rule.** Todo número que se atualiza usa `tnum`, para não tremer a cada leitura.

## Layout

Coluna única rolável com margem de tela de 8 dp sobre o rejunte. Os ladrilhos se empilham com junta de 4 dp; na grade, a junta vertical é uma view de 4 dp entre duas colunas de peso igual. A ordem da parede: ladrilho da marca de largura total (mínimo 96 dp), ladrilho de estado de largura total (mínimo 248 dp, conteúdo centralizado), grade 2x2 com três sensores e o ladrilho pintado (mínimo 168 dp, as duas peças de cada fileira com a mesma altura), depois ladrilhos de largura total para a barra da última hora, o resumo, um gráfico por sensor (150 dp de área) e o botão Desconectar.

Preenchimento interno: 16 dp no ladrilho comum, 20/16 dp no ladrilho da marca, 24 dp no ladrilho de estado. Títulos de seção ficam em linha com seu detalhe técnico alinhado pela linha de base à direita. Com fonte ampliada (130%) os ladrilhos crescem em altura; nada tem altura fixa além da barra (36 dp), dos gráficos e do botão Desconectar.

**The Junta Única Rule.** Entre quaisquer dois ladrilhos a distância é sempre a junta de 4 dp. Espaço maior se cria dentro do ladrilho, nunca alargando a junta.

## Elevation & Depth

Sem sombras e sem elevação (os botões têm o `stateListAnimator` desligado). A profundidade é material: cada ladrilho é uma camada de face sobre uma camada de base 2 dp mais alta embaixo, deixando um filete de esmalte mais escuro na borda inferior, como a espessura do vidrado. O ladrilho da marca, o ladrilho de perigo e cada ladrilho da barra usam a mesma construção com a base do seu tom.

**The Filete de Esmalte Rule.** Todo ladrilho, desenhado em XML ou em código, tem face com cantos de 3 dp e filete de 2 dp na base no tom `*_base` correspondente. É o único sinal de volume do sistema.

## Shapes

Retângulos quase retos: canto de 3 dp nos ladrilhos e botões, 2 dp nos ladrilhos pequenos da barra. Nada de pílulas, círculos de fundo ou cantos grandes. A borda de estado é um traço de 3 dp por dentro do ladrilho de estado. O ladrilho pintado é a única forma livre: quartos de círculo nos cantos, estrela de quatro pontas e anel, na cor da marca sobre a louça, recortado em `centerCrop` pela peça. O ícone do app repete o motivo num ladrilho branco sobre fundo marca, com versão monocromática.

## Components

### Ladrilho de estado (assinatura)
O ladrilho grande que responde "está tudo bem?".
- **Conteúdo:** ícone de 44 dp, palavra de estado em Display, mensagem em Body Large, "Última leitura às …" em 13sp; desconectado, o botão Conectar fica dentro dele.
- **Estados:** Normal, Atenção, Conectado (aguardando), Desconectado, Sem conexão, Sem leituras ficam na louça com borda de 3 dp na cor do estado e ícone tingido; Perigo vira ladrilho cheio vermelho com texto e ícone brancos. Atenção e Perigo alinham a mensagem à esquerda porque viram lista de ações; os demais centralizam.
- **Movimento:** ao mudar de estado, crossfade de alpha 0,35 para 1 em 280 ms com desaceleração. Com animações desligadas no sistema, a troca é direta.
- **Acessibilidade:** a palavra de estado é região viva assertiva.

### Ladrilho da marca (cabeçalho)
Nome do app em Title Large branco, linha de conexão em `marca_clara` (região viva educada) e, à direita, o botão de tema (ícone de paleta branco, alvo de 48 dp, ripple sem borda) que abre o seletor.

### Ladrilho de sensor
Nome da casa, nome técnico, espaço flexível, valor em Headline com unidade, frase de significado. Fora do ar, o valor passa para tinta fraca e a frase vira "Última leitura às …".

### Barra da última hora (assinatura)
Faixa de 12 ladrilhos pequenos assentados num leito de rejunte (juntas de 3 dp, cantos de 2 dp), cada um vitrificado na pior cor de estado do seu trecho, com filete de base. Trecho vazio é louça. Abaixo, "há …" e "agora" em Label e a linha de porcentagens por estado em Body tabular; a barra lê essa linha como descrição para leitor de tela.

### Gráfico de sensor
Um traço de 2 dp em `acao` com ponto de 3,5 dp no valor atual, linhas de base em `fio`, faixas de fundo pálidas onde houve atenção ou perigo e legenda dessas faixas só quando aparecem. Rótulos máx/mín/agora em Label tabular.

### Buttons
- **Shape:** ladrilho (cantos de 3 dp, filete de base), sem elevação; texto em caixa normal, Roboto Medium 16sp.
- **Primário (`botao-marca`):** ladrilho da marca com texto branco, altura mínima 52 dp, 28 dp de lado; ripple branco translúcido.
- **Secundário (`ladrilho-botao`):** ladrilho de louça de largura total, 64 dp, texto em `acao`; ripple em `toque`.
- **Diálogo:** botões sem borda na cor `acao`, 15sp, sem caixa alta.

### Lista do seletor de detector
Linha de 64 dp com 24 dp nas laterais: nome em Title, endereço em Label 13sp; fundo de seleção padrão da plataforma.

### Notificações
Ícone pequeno do motivo; cor da notificação fixa na cor da marca, dos alertas na cor do estado (`atencao_vidrado`, `perigo`, `sem_conexao`), com ícone de estado próprio e título que começa pela palavra de estado.

## Do's and Don'ts

### Do:
- **Do** assentar toda informação nova num ladrilho (`@style/Ladrilho` ou o helper de ladrilho em código), com junta de 4 dp.
- **Do** acompanhar toda cor de estado de ícone e palavra.
- **Do** mostrar "Sem conexão"/"Sem leituras" em cinza neutro e apagar valores antigos para tinta fraca com o horário da última leitura.
- **Do** usar `tnum` em todo número que muda.
- **Do** derivar o tema noturno trocando apenas os valores de `values-night/colors.xml`, mantendo os mesmos nomes.

### Don't:
- **Don't** usar sombra ou elevação; o volume vem só do filete de esmalte.
- **Don't** pintar um ladrilho inteiro com cor de estado que não seja perigo.
- **Don't** usar cantos maiores que 3 dp ou formas de pílula.
- **Don't** criar cartões soltos com margens diferentes da junta, nem medidores circulares de painel de casa inteligente.
- **Don't** usar o motivo na cor da marca como fundo atrás de texto; ele vive só no ladrilho pintado e no ícone.
- **Don't** animar nada além do crossfade do ladrilho de estado.
