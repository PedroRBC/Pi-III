package com.pi3.detector

import java.util.Locale
import kotlin.random.Random

// Modo demonstração: só aparece no build debug (BuildConfig.DEBUG) e simula o Arduino sem o HC-05.
const val ENDERECO_DEMO = "demo"

// Uma linha por segundo no mesmo formato do sketch: normal → atenção → perigo → normal.
// Quando a sequência acaba o serviço trata como conexão perdida e reconecta, como no HC-05 real.
fun linhasDemo(): Sequence<String> = sequence {
    for ((segundos, fase) in listOf(15 to 0, 12 to 1, 12 to 2, 8 to 0)) repeat(segundos) {
        Thread.sleep(1000)
        val mq2: Int
        val mq135: Int
        val temp: Float
        when (fase) {
            2 -> { mq2 = 650 + Random.nextInt(150); mq135 = 420 + Random.nextInt(150); temp = 58f + Random.nextFloat() * 8 }
            1 -> { mq2 = 320 + Random.nextInt(200); mq135 = 250 + Random.nextInt(150); temp = 36f + Random.nextFloat() * 6 }
            else -> { mq2 = 150 + Random.nextInt(100); mq135 = 120 + Random.nextInt(100); temp = 24f + Random.nextFloat() * 3 }
        }
        // mesma regra do sketch: limites 300/600 nos MQ e 40/60 °C
        val estado = when {
            mq2 >= 600 || mq135 >= 600 || temp >= 60 -> 2
            mq2 >= 300 || mq135 >= 300 || temp >= 40 -> 1
            else -> 0
        }
        // Locale.US: o Arduino sempre envia ponto decimal
        yield(String.format(Locale.US, "%d,%d,%.2f,%d", mq2, mq135, temp, estado))
    }
}
