// ===============================
// PINOS
// ===============================

const int MQ2 = A0;
const int MQ135 = A1;
const int LM35DZ = A2;

const int LED = 8;
const int BUZZER = 9;


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

  digitalWrite(LED, LOW);
  noTone(BUZZER);

  Serial.println("================================");
  Serial.println(" DETECTOR DE GAS E INCENDIO");
  Serial.println(" (VERSAO COM LM35DZ)");
  Serial.println("================================");
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


  // Se temperatura estiver muito alta
  // OU houver grande quantidade de gases
  if (temperaturaPerigosa || gasesPerigosos) {

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

  else if (
    temperatura >= TEMP_ATENCAO ||
    valorMQ2 >= MQ2_ATENCAO ||
    valorMQ135 >= MQ135_ATENCAO
  ) {

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
