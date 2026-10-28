# Codigos-Pi-III

Repositório com códigos Arduino do projeto.

## Código disponível

- `detector_gas_incendio.ino`: detector de gás e incêndio usando MQ-2, MQ-135, DHT11, LED e buzzer.
- `detector_gas_incendio_lm35dz.ino`: versão similar usando MQ-2, MQ-135, LM35DZ, LED, buzzer e módulo Bluetooth HC-05.
- `app-android/`: app Android **Detector de Gases**, que recebe os dados do HC-05, mostra os valores, gera alertas no celular e exibe estatísticas.

## Ligação do HC-05 (versão LM35DZ)

| HC-05 | Arduino |
|-------|---------|
| VCC   | 5V |
| GND   | GND |
| TXD   | pino 10 |
| RXD   | pino 11 **via divisor de tensão** (ex.: 1 kΩ do pino 11 ao RXD, 2 kΩ do RXD ao GND), pois o RXD do HC-05 é 3,3 V |

A cada leitura o Arduino envia pelo Bluetooth uma linha `mq2,mq135,temperatura,estado`
(ex.: `312,280,25.40,1`), onde estado é `0` normal, `1` atenção e `2` perigo.
Os limites continuam definidos no próprio sketch.

## App Android

O HC-05 usa Bluetooth Clássico (SPP), então o app é Android nativo (iPhone não conversa com o HC-05).

Recursos:
- conexão com o HC-05 pareado, com reconexão automática a cada 5 s;
- alerta no celular (notificação com vibração) quando o estado passa para ATENÇÃO ou PERIGO, e quando a conexão cai, inclusive com a tela bloqueada;
- valores atuais, estatísticas da sessão (mínimo, média, máximo e % do tempo em cada estado) e gráficos de MQ-2, MQ-135 e temperatura da última hora;
- tema claro e escuro, seletor de cor (roxo e cinza por padrão, azul azulejo, petróleo e grafite), textos em português e o que fazer em cada estado (ex.: em perigo, fechar o registro do gás e ligar 193).

O app se chama **Detector de Gases**. Capturas de tela em `app-android/screenshots/`; as decisões visuais estão em `DESIGN.md`.

Nome do detector no Bluetooth (uma vez por módulo):
1. Segure o botão do HC-05 enquanto liga o Arduino. O sketch grava o nome `DetectorGases-01` (constante `NOME_BLUETOOTH`) e o LED pisca devagar; se piscar rápido, falhou (veja o Monitor Serial).
2. Desligue e ligue o Arduino sem segurar o botão.

Para um segundo detector, troque o final do nome (ex.: `DetectorGases-02`). O app só lista aparelhos cujo nome começa com `DetectorGases`, então fones, relógios e outros aparelhos pareados não aparecem.

Uso:
1. Pareie o `DetectorGases-01` nas configurações de Bluetooth do celular (PIN `1234` ou `0000`). Se ele já estava pareado como HC-05, toque em **Esquecer** e pareie de novo, para o celular pegar o nome novo.
2. Instale o app, toque em **Conectar ao detector** e escolha o detector.

No build debug a lista também mostra **Modo demonstração**, que simula o Arduino (normal → atenção → perigo → queda de conexão) para testar sem hardware.

Compilar (requer JDK 17 e Android SDK):

```sh
cd app-android
./gradlew assembleDebug        # gera app/build/outputs/apk/debug/app-debug.apk
adb install app/build/outputs/apk/debug/app-debug.apk
```
