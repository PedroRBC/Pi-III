# Product

<!-- impeccable:product-schema 1 -->

## Platform

android

## Users

Moradores comuns, em casa. O detector fica na cozinha, perto do fogão ou do botijão de gás (GLP). A pessoa não sabe o que é um MQ-2 ou um MQ-135 e precisa entender um alerta na hora, sem ler números.

## Product Purpose

Avisar no celular quando há risco de vazamento de gás ou de incêndio na cozinha, e deixar a pessoa conferir como está o ar quando quiser. Sucesso é o alerta chegar a tempo e ser entendido de imediato; o resto (valores, histórico) é consulta ocasional.

## Operating Context

- O celular fica no bolso a maior parte do tempo. O principal canal é a notificação, inclusive com a tela bloqueada; o app é aberto de vez em quando para conferir.
- O Arduino (MQ-2, MQ-135, LM35DZ, LED e buzzer) decide o estado e envia pelo HC-05 (Bluetooth Clássico/SPP) uma linha por leitura: `mq2,mq135,temperatura,estado`, com estado 0 = normal, 1 = atenção, 2 = perigo.
- O pareamento com o HC-05 é feito nas configurações de Bluetooth do Android (PIN 1234 ou 0000).
- Projeto acadêmico (Projeto Integrador III), mas o app deve se comportar como produto para uso doméstico.

## Capabilities and Constraints

- App Android nativo em Kotlin, `android.app.Activity` com layouts XML, sem AndroidX e sem bibliotecas de terceiros; sem servidor e sem internet.
- Só Android: o HC-05 usa SPP, que o iPhone não suporta.
- Recursos: conexão com reconexão automática; notificação quando o estado sobe para atenção ou perigo e quando a conexão cai; valores atuais; estatísticas da sessão (mín/média/máx, % do tempo em cada estado); gráficos da última hora.
- Os limites de atenção/perigo vivem no sketch do Arduino; o app só exibe o estado recebido.
- Histórico apenas em memória (até ~1 h); perdido ao desconectar.
- Build debug inclui "Modo demonstração" com dados simulados.

## Brand Commitments

- Nome do produto e do dispositivo: **Detector de Gases**.
- Interface em português do Brasil.
- Sem logo, ícone ou cores pré-existentes: identidade a ser criada.

## Evidence on Hand

- Capturas do app atual em `app-android/screenshots/`.
- Não há usuários, depoimentos, certificações ou testes de campo: não inventar alegações de segurança, precisão ou certificação.

## Product Principles

1. O estado vem antes do número: normal, atenção, perigo e sem conexão precisam ser entendidos em um olhar, por quem não conhece os sensores.
2. A notificação é a interface principal; a tela do app é consulta.
3. Nunca passar falsa segurança: sem conexão ou sem leituras recentes nunca aparece como "normal".
4. Falar a língua da casa (gás, fumaça, temperatura), mantendo os nomes técnicos como detalhe secundário.

## Accessibility & Inclusion

Estados não podem depender só de cor (texto e ícone sempre juntos); textos legíveis com fonte ampliada do sistema; alertas acessíveis por leitor de tela.
