# Montagem do Detector de Gases

Guia de montagem da versão `detector_gas_incendio_lm35dz.ino` (MQ-2, MQ-135, LM35DZ, LED, buzzer e Bluetooth HC-05). Os pinos valem para Arduino Uno e Nano.

## Materiais

| Qtd | Componente                                                      | Para quê                         |
| --- | --------------------------------------------------------------- | -------------------------------- |
| 1   | Arduino Uno (ou Nano)                                           | Lê os sensores e decide o estado |
| 1   | Módulo MQ-2 (placa com 4 pinos: VCC, GND, DO, AO)               | Gás de cozinha e fumaça          |
| 1   | Módulo MQ-135 (placa com 4 pinos: VCC, GND, DO, AO)             | Qualidade do ar                  |
| 1   | Sensor de temperatura LM35DZ (3 pernas)                         | Calor perto do fogão             |
| 1   | LED (vermelho, 5 mm)                                            | Aviso visual                     |
| 1   | Buzzer passivo                                                  | Aviso sonoro                     |
| 1   | Módulo Bluetooth HC-05 (6 pinos: EN, VCC, GND, TXD, RXD, STATE) | Envia as leituras para o app     |
| 1   | Resistor de 220 Ω                                               | Limita a corrente do LED         |
| 1   | Resistor de 1 kΩ                                                | Divisor de tensão do HC-05       |
| 1   | Resistor de 2 kΩ (ou dois de 1 kΩ em série)                     | Divisor de tensão do HC-05       |
| 1   | Protoboard e jumpers                                            | Ligações                         |
| 1   | Carregador de celular USB, 5 V e 1 A ou mais                    | Alimentação                      |

## 1. Alimentação na protoboard

1. Ligue o pino **5V** do Arduino na trilha vermelha (+) da protoboard.
2. Ligue um pino **GND** do Arduino na trilha azul (−).
3. Todos os componentes abaixo pegam 5 V e GND dessas trilhas. Todos os GND precisam estar ligados juntos.

## 2. Sensor MQ-2 (gás e fumaça)

| Pino do MQ-2 | Liga em |
|--------------|---------|
| VCC | 5 V (trilha +) |
| GND | GND (trilha −) |
| AO (saída analógica) | **A0** do Arduino |
| DO | não liga |

## 3. Sensor MQ-135 (qualidade do ar)

| Pino do MQ-135 | Liga em |
|----------------|---------|
| VCC | 5 V (trilha +) |
| GND | GND (trilha −) |
| AO (saída analógica) | **A1** do Arduino |
| DO | não liga |

Os dois MQ têm um aquecedor interno e esquentam bastante. Não encoste neles e deixe os fios longe da cápsula metálica.

## 4. Sensor de temperatura LM35DZ

Segure o LM35DZ com a **face reta virada para você** e as pernas para baixo:

| Perna | Liga em |
|-------|---------|
| Esquerda (+Vs) | 5 V (trilha +) |
| Meio (Vout) | **A2** do Arduino |
| Direita (GND) | GND (trilha −) |

Se ele esquentar sozinho ao ligar, está invertido. Desligue na hora e vire o sensor.

## 5. LED com resistor

O LED precisa de um resistor em série, senão queima o LED e força o pino do Arduino.

1. Ligue o **pino 8** do Arduino em uma ponta do **resistor de 220 Ω**.
2. Ligue a outra ponta do resistor na **perna longa** do LED (ânodo, +).
3. Ligue a **perna curta** do LED (cátodo, −, do lado chanfrado) no GND.

```
pino 8 ──[ 220 Ω ]──►|── GND
                  ânodo  cátodo
              (perna longa) (perna curta)
```

Com 220 Ω passam cerca de 13 mA. Um resistor de 330 Ω também serve, com o LED um pouco mais fraco.

## 6. Buzzer

| Pino do buzzer | Liga em |
|----------------|---------|
| + (perna longa ou marcada com +) | **pino 9** do Arduino |
| − | GND (trilha −) |

Use buzzer **passivo**, sem oscilador interno: o sketch gera o tom com `tone()`, 1000 Hz na atenção e 2000 Hz no perigo. Um buzzer ativo também apita, mas com som áspero.

## 7. Bluetooth HC-05 com divisor de tensão

| Pino do HC-05 | Liga em |
|---------------|---------|
| VCC | 5 V (trilha +) |
| GND | GND (trilha −) |
| TXD | **pino 10** do Arduino, direto |
| RXD | **pino 11** do Arduino, **pelo divisor de tensão** (abaixo) |
| EN | não liga |
| STATE | não liga |

A ligação é cruzada: o TXD (transmissão) do módulo vai no pino 10, que é a recepção do Arduino no sketch. O RXD (recepção) vai no pino 11, que é a transmissão.

### Divisor de tensão do RXD

O RXD do HC-05 aceita **3,3 V**, mas o pino 11 do Arduino envia 5 V. Os dois resistores baixam a tensão:

1. Ligue o **resistor de 1 kΩ** entre o **pino 11** do Arduino e o **RXD** do HC-05.
2. Ligue o **resistor de 2 kΩ** entre o **RXD** do HC-05 e o **GND**.

```
pino 11 ──[ 1 kΩ ]──┬── RXD do HC-05
                    │
                 [ 2 kΩ ]
                    │
                   GND
```

No ponto do meio ficam cerca de 3,3 V (5 V × 2 kΩ / 3 kΩ). Sem resistor de 2 kΩ, use dois de 1 kΩ em série ou um de 2,2 kΩ. O TXD não precisa de resistor: o Arduino entende os 3,3 V que vêm dele.

## Resumo de pinos do Arduino

| Pino | Componente |
|------|------------|
| A0 | MQ-2 (AO) |
| A1 | MQ-135 (AO) |
| A2 | LM35DZ (perna do meio) |
| 8 | LED, pelo resistor de 220 Ω |
| 9 | Buzzer (+) |
| 10 | TXD do HC-05 |
| 11 | RXD do HC-05, pelo divisor 1 kΩ / 2 kΩ |
| 5V | trilha +: MQ-2, MQ-135, LM35DZ e HC-05 |
| GND | trilha −: todos os GND, LED, buzzer e o resistor de 2 kΩ |

Não use os pinos 0 e 1: são da USB, usados pelo Monitor Serial.

## Antes de ligar

- [ ] LM35DZ com a face reta para você: 5 V à esquerda, GND à direita.
- [ ] Perna longa do LED no lado do resistor; perna curta no GND.
- [ ] Divisor de 1 kΩ e 2 kΩ no RXD do HC-05.
- [ ] TXD do HC-05 no pino 10 e RXD no 11, cruzados.
- [ ] Nenhum fio ligando direto a trilha + na trilha −.

## Alimentação

Os dois MQ consomem cerca de 150 mA cada, e o conjunto chega perto de 400 mA. Alimente pela **USB com um carregador de celular de 5 V e 1 A ou mais**. Evite bateria de 9 V, que acaba em poucas horas, e fonte de 12 V no conector redondo, que esquenta o regulador do Arduino.

## Primeiro uso

1. **Aquecimento dos MQ.** Sensores novos precisam de cerca de 24 h ligados para estabilizar. Depois disso, a cada vez que o detector é ligado, espere de 1 a 3 minutos: as leituras começam altas e podem marcar atenção até o aquecedor esquentar.
2. **Teste pelo computador.** Abra o Monitor Serial a 9600 baud. Devem aparecer os valores de MQ-2, MQ-135 e temperatura a cada segundo.
3. **LED do HC-05.** Piscando rápido, está esperando o celular. Com duas piscadas a cada ~2 s, está conectado ao app.
4. **Nome no Bluetooth (uma vez por módulo).** Segure o botão do HC-05 enquanto liga o Arduino. O sketch grava o nome `DetectorGases-01` e o LED do pino 8 pisca devagar; se piscar rápido, falhou. Depois desligue e ligue sem o botão. Se o seu módulo não tiver botão, ligue o EN no 3.3V do Arduino só nessa hora e desligue o EN depois.
5. **Parear e conectar.** Pareie o `DetectorGases-01` no Bluetooth do celular (PIN `1234` ou `0000`). No app, toque em **Conectar ao detector** e escolha o detector.
