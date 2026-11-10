#include <SoftwareSerial.h>

// ===============================
// PINOS
// ===============================

const int MQ2 = A0;
const int MQ135 = A1;
const int LM35DZ = A2;

const int LED = 8;
const int BUZZER = 9;

// HC-05: TX do modulo -> pino 10 | RX do modulo <- pino 11
// O RX do HC-05 e 3,3 V: use divisor de tensao (ex.: 1k + 2k) no pino 11
const int BT_RX = 10;
const int BT_TX = 11;

SoftwareSerial bluetooth(BT_RX, BT_TX);

// Nome do detector no Bluetooth do celular. O app so lista aparelhos
// cujo nome comeca com "DetectorGases"; para um segundo detector use -02.
const char NOME_BLUETOOTH[] = "DetectorGases-01";


// ===============================
// LIMITES DOS SENSORES
// ===============================

// MQ-2
const int MQ2_ATENCAO = 300;
const int MQ2_PERIGO = 600;

// MQ-135
const int MQ135_ATENCAO = 300;
const int MQ135_PERIGO = 600;

// Temperatura
const float TEMP_ATENCAO = 40.0;
const float TEMP_PERIGO = 60.0;


// ===============================
// CONFIGURACAO
// ===============================

void setup() {
  pinMode(LED, OUTPUT);
  pinMode(BUZZER, OUTPUT);

  Serial.begin(9600);
  gravarNomeBluetooth();
  bluetooth.begin(9600); // baud padrao do HC-05 em modo de dados

  digitalWrite(LED, LOW);
  noTone(BUZZER);

  Serial.println("================================");
  Serial.println(" DETECTOR DE GAS E INCENDIO");
  Serial.println(" (VERSAO COM LM35DZ)");
  Serial.println("================================");
}


// ===============================
// NOME DO HC-05 (feito uma vez)
// ===============================

// Para gravar: segure o botao do HC-05 enquanto liga o Arduino. O modulo
// entra em modo AT (38400 baud), o sketch grava o nome e o LED pisca:
// devagar = gravado, rapido = falhou. Depois desligue e ligue sem o botao.
// Ligado normalmente, o modulo nao responde "OK" e o detector segue.

// Envia um comando AT e espera "OK" por ate 1 s.
bool comandoAT(String comando) {
  bluetooth.print(comando);
  bluetooth.print("\r\n");
  String resposta = "";
  unsigned long inicio = millis();
  while (millis() - inicio < 1000) {
    while (bluetooth.available()) {
      resposta += (char) bluetooth.read();
    }
    if (resposta.indexOf("OK") >= 0) {
      return true;
    }
  }
  Serial.print(comando);
  Serial.print(" falhou: ");
  Serial.println(resposta);
  return false;
}

void gravarNomeBluetooth() {
  delay(1000); // tempo do HC-05 iniciar
  bluetooth.begin(38400);
  if (!comandoAT("AT")) {
    bluetooth.end(); // modo normal: nada a gravar
    return;
  }

  bool gravado =
    comandoAT(String("AT+NAME=") + NOME_BLUETOOTH) &&
    comandoAT("AT+UART=9600,0,0"); // baud do modo de dados usado no loop

  Serial.println(gravado
    ? "HC-05 gravado. Desligue e ligue o Arduino sem segurar o botao."
    : "Falha ao gravar o HC-05. Confira a ligacao e tente de novo.");

  // em modo AT o modulo nao envia dados: fica aqui ate desligar
  int pisca = gravado ? 1000 : 150;
  while (true) {
    digitalWrite(LED, !digitalRead(LED));
    delay(pisca);
  }
}


// ===============================
// LOOP PRINCIPAL
// ===============================

void loop() {

  // Leitura dos sensores
  int valorMQ2 = analogRead(MQ2);
  int valorMQ135 = analogRead(MQ135);
  int valorLM35 = analogRead(LM35DZ);

  // LM35DZ: 10 mV por grau Celsius
  float tensao = valorLM35 * (5.0 / 1023.0);
  float temperatura = tensao * 100.0;


  // ===============================
  // MOSTRA OS VALORES
  // ===============================

  Serial.println("--------------------------------");

  Serial.print("MQ-2: ");
  Serial.println(valorMQ2);

  Serial.print("MQ-135: ");
  Serial.println(valorMQ135);

  Serial.print("Temperatura: ");
  Serial.print(temperatura);
  Serial.println(" C");


  // ===============================
  // DETECCAO DE POSSIVEL INCENDIO
  // ===============================

  bool temperaturaPerigosa =
    temperatura >= TEMP_PERIGO;

  bool gasesPerigosos =
    valorMQ2 >= MQ2_PERIGO ||
    valorMQ135 >= MQ135_PERIGO;

  bool atencao =
    temperatura >= TEMP_ATENCAO ||
    valorMQ2 >= MQ2_ATENCAO ||
    valorMQ135 >= MQ135_ATENCAO;

  // 0 = normal, 1 = atencao, 2 = perigo
  int estado = 0;
  if (temperaturaPerigosa || gasesPerigosos) {
    estado = 2;
  } else if (atencao) {
    estado = 1;
  }


  // ===============================
  // ENVIA PARA O APP (HC-05)
  // ===============================

  // Formato: mq2,mq135,temperatura,estado  (ex.: 312,280,25.40,1)
  bluetooth.print(valorMQ2);
  bluetooth.print(',');
  bluetooth.print(valorMQ135);
  bluetooth.print(',');
  bluetooth.print(temperatura);
  bluetooth.print(',');
  bluetooth.println(estado);


  // Se temperatura estiver muito alta
  // OU houver grande quantidade de gases
  if (estado == 2) {

    digitalWrite(LED, HIGH);

    tone(BUZZER, 2000);

    Serial.println("!!! ALERTA DE PERIGO !!!");

    if (temperaturaPerigosa) {
      Serial.println("Temperatura muito elevada!");
    }

    if (valorMQ2 >= MQ2_PERIGO) {
      Serial.println("Nivel elevado detectado pelo MQ-2!");
    }

    if (valorMQ135 >= MQ135_PERIGO) {
      Serial.println("Nivel elevado detectado pelo MQ-135!");
    }

    delay(1000);
  }


  // ===============================
  // ATENCAO
  // ===============================

  else if (estado == 1) {

    Serial.println(">>> ATENCAO <<<");

    digitalWrite(LED, HIGH);

    tone(BUZZER, 1000);

    delay(250);

    digitalWrite(LED, LOW);

    noTone(BUZZER);

    delay(500);
  }


  // ===============================
  // SITUACAO NORMAL
  // ===============================

  else {

    Serial.println(">>> Ambiente normal");

    digitalWrite(LED, LOW);

    noTone(BUZZER);

    delay(1000);
  }
}
