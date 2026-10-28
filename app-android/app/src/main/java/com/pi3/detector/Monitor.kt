package com.pi3.detector

// Estado compartilhado entre o MonitorService (thread Bluetooth) e a MainActivity.
// shortcut: histórico só em memória (perde ao fechar o serviço); salvar em arquivo/Room se precisar de dias de histórico.
object Monitor {
    private const val MAX_LEITURAS = 3600 // ~1 h com uma leitura por segundo
    private val leituras = ArrayDeque<Leitura>()

    @Volatile var conexao = "Desconectado"
    @Volatile var conectado = false
    @Volatile var ativo = false // serviço rodando (conectado ou tentando reconectar)
    @Volatile var ouvinte: (() -> Unit)? = null

    @Synchronized fun adicionar(l: Leitura) {
        if (leituras.size == MAX_LEITURAS) leituras.removeFirst()
        leituras.addLast(l)
    }

    @Synchronized fun copia(): List<Leitura> = leituras.toList()

    fun avisar() = ouvinte?.invoke()
}
