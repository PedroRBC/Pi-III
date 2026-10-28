package com.pi3.detector

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.view.ContextThemeWrapper
import java.io.IOException
import java.util.UUID
import kotlin.concurrent.thread

// Mantém a conexão com o HC-05 em primeiro plano para os alertas chegarem com a tela bloqueada.
// A MainActivity só inicia este serviço depois de obter BLUETOOTH_CONNECT.
@SuppressLint("MissingPermission")
class MonitorService : Service() {

    @Volatile private var rodando = true
    @Volatile private var socket: BluetoothSocket? = null
    private var leitor: Thread? = null
    private var ultimoEstado = 0

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val endereco = intent?.getStringExtra(EXTRA_ENDERECO)
        if (endereco == null) {
            stopSelf()
            return START_NOT_STICKY
        }
        criarCanais()
        Monitor.ativo = true
        val notif = notificacao("Conectando ao detector…")
        if (Build.VERSION.SDK_INT >= 29) {
            startForeground(ID_MONITOR, notif, ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
        } else {
            startForeground(ID_MONITOR, notif)
        }
        if (leitor == null) leitor = thread(name = "hc05") { executar(endereco) }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        rodando = false
        runCatching { socket?.close() }
        leitor?.interrupt()
        Monitor.conexao = "Desconectado"
        Monitor.conectado = false
        Monitor.ativo = false
        Monitor.avisar()
    }

    private fun executar(endereco: String) {
        val dispositivo = if (endereco == ENDERECO_DEMO) null
            else getSystemService(BluetoothManager::class.java).adapter.getRemoteDevice(endereco)
        val nome = if (dispositivo == null) "Modo demonstração" else dispositivo.name ?: endereco
        var avisouQueda = false
        while (rodando) {
            try {
                atualizarConexao("Conectando ao detector · $nome…")
                val linhas = if (dispositivo == null) linhasDemo() else {
                    val s = dispositivo.createRfcommSocketToServiceRecord(SPP_UUID)
                    socket = s
                    s.connect()
                    s.inputStream.bufferedReader().lineSequence()
                }
                avisouQueda = false
                getSystemService(NotificationManager::class.java).cancel(ID_QUEDA)
                atualizarConexao("Conectado ao detector · $nome", true)
                for (linha in linhas) {
                    if (!rodando) break
                    parseLeitura(linha)?.let { receber(it) }
                }
            } catch (_: IOException) {
                // conexão caiu ou HC-05 fora de alcance: tenta de novo abaixo
            } catch (_: InterruptedException) {
                break // serviço parado durante o modo demonstração
            }
            runCatching { socket?.close() }
            if (!rodando) break
            if (!avisouQueda) {
                alertar(
                    ID_QUEDA, "Sem conexão com o detector",
                    "Os alertas não chegam enquanto ele estiver desconectado. Tentando reconectar…",
                    R.drawable.ic_sem_conexao, R.color.sem_conexao,
                )
                avisouQueda = true
            }
            atualizarConexao("Sem conexão. Tentando de novo…")
            try {
                Thread.sleep(5000)
            } catch (_: InterruptedException) {
                break
            }
        }
    }

    private fun receber(l: Leitura) {
        Monitor.adicionar(l)
        if (l.estado > ultimoEstado) {
            val valores = "Gás e fumaça ${l.mq2} · Qualidade do ar ${l.mq135} · %.1f °C".format(l.temperatura)
            if (l.estado == 2) {
                // um alerta por vez: o perigo substitui o aviso de atenção
                getSystemService(NotificationManager::class.java).cancel(ID_ALERTA + 1)
                alertar(
                    ID_ALERTA + 2, "Perigo: possível vazamento de gás ou incêndio",
                    "Feche o registro do gás, não acione interruptores e saia de casa. Emergência: 193.",
                    R.drawable.ic_perigo, R.color.perigo, valores,
                )
            } else {
                alertar(
                    ID_ALERTA + 1, "Atenção: o ar da cozinha mudou",
                    "Abra as janelas e confira se o fogão e o gás estão fechados.",
                    R.drawable.ic_atencao, R.color.atencao_vidrado, valores,
                )
            }
        }
        ultimoEstado = l.estado
        Monitor.avisar()
    }

    private fun atualizarConexao(texto: String, conectado: Boolean = false) {
        Monitor.conexao = texto
        Monitor.conectado = conectado
        getSystemService(NotificationManager::class.java).notify(ID_MONITOR, notificacao(texto))
        Monitor.avisar()
    }

    private fun alertar(id: Int, titulo: String, texto: String, icone: Int, cor: Int, detalhe: String? = null) {
        val n = Notification.Builder(this, CANAL_ALERTAS)
            .setSmallIcon(icone)
            .setColor(getColor(cor))
            .setContentTitle(titulo)
            .setContentText(texto)
            .setStyle(Notification.BigTextStyle().bigText(if (detalhe == null) texto else "$texto\n$detalhe"))
            .setGroup("alertas") // grupo próprio: o sistema não mistura o alerta com a notificação fixa
            .setVisibility(Notification.VISIBILITY_PUBLIC) // texto visível com a tela bloqueada
            .apply { if (icone == R.drawable.ic_perigo) setCategory(Notification.CATEGORY_ALARM) } // passa pelo Não perturbe
            .setContentIntent(abrirApp())
            .setAutoCancel(true)
            .setOnlyAlertOnce(true) // evita vibrar a cada segundo se o valor oscilar perto do limite
            .build()
        getSystemService(NotificationManager::class.java).notify(id, n)
    }

    private fun notificacao(texto: String): Notification =
        Notification.Builder(this, CANAL_MONITOR)
            .setSmallIcon(R.drawable.ic_notificacao)
            .setColor(ContextThemeWrapper(this, temaAtual().estilo).corDoTema(R.attr.marca))
            .setContentTitle("Detector de Gases")
            .setGroup("monitor")
            .setContentText(texto)
            .setContentIntent(abrirApp())
            .setOngoing(true)
            .build()

    private fun abrirApp(): PendingIntent = PendingIntent.getActivity(
        this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_IMMUTABLE,
    )

    private fun criarCanais() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(
            NotificationChannel(CANAL_MONITOR, "Conexão com o detector", NotificationManager.IMPORTANCE_LOW),
        )
        nm.createNotificationChannel(
            NotificationChannel(CANAL_ALERTAS, "Alertas de gás e incêndio", NotificationManager.IMPORTANCE_HIGH)
                .apply { enableVibration(true) },
        )
    }

    companion object {
        const val EXTRA_ENDERECO = "endereco"
        private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        private const val CANAL_MONITOR = "monitor"
        private const val CANAL_ALERTAS = "alertas"
        private const val ID_MONITOR = 1
        private const val ID_QUEDA = 2
        private const val ID_ALERTA = 10 // + estado (11 = atenção, 12 = perigo)
    }
}
