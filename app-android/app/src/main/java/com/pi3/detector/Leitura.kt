package com.pi3.detector

data class Leitura(
    val mq2: Int,
    val mq135: Int,
    val temperatura: Float,
    val estado: Int,
    val em: Long = System.currentTimeMillis(),
)

// Nome que o sketch grava no HC-05 (NOME_BLUETOOTH); a lista mostra só os pareados com esse prefixo.
const val PREFIXO_DETECTOR = "DetectorGases"

// Linha enviada pelo Arduino: "mq2,mq135,temperatura,estado", ex.: "312,280,25.40,1".
// Retorna null para linhas corrompidas (comum logo após conectar o HC-05).
fun parseLeitura(linha: String): Leitura? {
    val p = linha.trim().split(',')
    if (p.size != 4) return null
    val mq2 = p[0].toIntOrNull()?.takeIf { it in 0..1023 } ?: return null
    val mq135 = p[1].toIntOrNull()?.takeIf { it in 0..1023 } ?: return null
    val temperatura = p[2].toFloatOrNull()?.takeIf { it in -50f..550f } ?: return null
    val estado = p[3].toIntOrNull()?.takeIf { it in 0..2 } ?: return null
    return Leitura(mq2, mq135, temperatura, estado)
}

// Pior estado de cada trecho da sessão, para a barra da última hora.
// Com menos leituras que trechos, os trechos mais antigos ficam vazios (null).
fun piorPorTrecho(estados: List<Int>, trechos: Int): List<Int?> {
    if (estados.size < trechos) return List(trechos - estados.size) { null } + estados
    return List(trechos) { i -> estados.subList(i * estados.size / trechos, (i + 1) * estados.size / trechos).max() }
}

fun duracao(ms: Long): String = when {
    ms < 60_000 -> "${ms / 1000} s"
    ms < 3_600_000 -> "${ms / 60_000} min"
    else -> "1 h"
}
