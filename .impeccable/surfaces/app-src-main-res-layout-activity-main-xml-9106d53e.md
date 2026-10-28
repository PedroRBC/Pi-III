---
version: 1
slug: "app-src-main-res-layout-activity-main-xml-9106d53e"
primary_target: "app-android/app/src/main/res/layout/activity_main.xml"
related_targets: ["app-android/app/src/main/java/com/pi3/detector/MainActivity.kt"]
---

# Surface brief: tela principal do app Android

Scope: app Android Detector de Gases (tela principal, seletor de detector, notificações, ícone). Visitor mode: Operate.
Audience e tarefa: morador em casa, detector na cozinha; abre o app de vez em quando para conferir o ar; a notificação é o canal principal. Sucesso: entender o estado num olhar e saber o que fazer.
Constraints: Android Views + XML, sem AndroidX/bibliotecas; estados nunca só por cor; sem conexão nunca parece normal; nada de cara de brinquedo, de template genérico ou de alarme exagerado.
Memorable moment: a barra da última hora em ladrilhos.
Unresolved: nenhum.

## Direction contract

THESIS: A tela é a parede de azulejos atrás do fogão: cada informação no seu ladrilho e o estado do ar no ladrilho grande. Recusa o painel escuro de casa inteligente com medidores e o app genérico de cartões soltos.

OWN-WORLD: Rejunte cinza-claro como fundo, ladrilhos brancos vitrificados (canto 3 dp, filete de esmalte na base, juntas de 4 dp), um ladrilho de cabeçalho pintado em cobalto e um ladrilho decorativo com motivo geométrico cobalto desenhado em vetor. Tinta grafite, Roboto, números tabulares. Cor de estado só na borda do ladrilho de estado; só o perigo vira ladrilho cheio vermelho. À noite: rejunte grafite e ladrilho cinza-esmalte, mesmo cobalto.

STORY: Quem abre entende em um olhar se o ar está normal e, se não estiver, o que fazer agora; depois, se quiser, lê cada sensor pelo nome da casa e vê a última hora.

FIRST VIEWPORT: Ladrilho cobalto de largura total no topo com Detector de Gases e a linha de conexão. Abaixo, o ladrilho de estado de largura total com ícone, a palavra do estado em 40 sp e a frase do que fazer; desconectado, o botão Conectar ao detector fica dentro dele. Depois, grade 2x2: Gás e fumaça (MQ-2), Qualidade do ar (MQ-135), Temperatura (LM35) e o ladrilho pintado. A barra da última hora começa logo abaixo.

SIGNATURE & MOTION: Barra da última hora: faixa de 12 ladrilhos pequenos, cada um vitrificado na pior cor de estado do seu trecho, como a barra decorativa da parede, com o % do tempo em cada estado. Gráficos com um traço por sensor e faixas de estado ao fundo marcando quando começou. Único movimento autoral: o ladrilho de estado faz um crossfade curto ao mudar de estado; com animações desligadas, troca direta.

FORM: Parede de azulejo, candidato 1 da lista ordenada, escolhido pelo usuário no cartão IMPECCABLE’S PICK. seed:5dd5e9f848b11d35

FINISH: unreviewed and undocumented is unfinished; this build ends with the finish review, the verdict, DESIGN.md, and every shipping raster carrying its provenance
