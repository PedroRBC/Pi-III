package com.pi3.detector

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.SpannableString
import android.text.format.DateFormat
import android.text.style.ImageSpan
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.ImageView
import android.widget.SimpleAdapter
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast

@SuppressLint("MissingPermission", "SetTextI18n") // permissão checada em temPermissaoBluetooth()
class MainActivity : Activity() {

    // nome da casa, nome técnico, o que significa, unidade, casas decimais
    private class Sensor(val nome: String, val tecnico: String, val significado: String, val unidade: String, val casas: Int)

    private val sensores = listOf(
        Sensor("Gás e fumaça", "MQ-2", "Botijão, fogão e fumaça", "de 1023", 0),
        Sensor("Qualidade do ar", "MQ-135", "Poluentes e cheiros", "de 1023", 0),
        Sensor("Temperatura", "LM35", "Calor perto do fogão", "°C", 1),
    )

    private lateinit var txtConexao: TextView
    private lateinit var ladrilhoEstado: View
    private lateinit var icEstado: ImageView
    private lateinit var txtEstado: TextView
    private lateinit var txtMensagem: TextView
    private lateinit var txtAtualizado: TextView
    private lateinit var btnConectar: Button
    private lateinit var btnDesconectar: Button
    private lateinit var valores: List<TextView>
    private lateinit var unidades: List<TextView>
    private lateinit var significados: List<TextView>
    private lateinit var txtFim: TextView
    private lateinit var barra: BarraEstados
    private lateinit var txtPeriodo: TextView
    private lateinit var txtInicio: TextView
    private lateinit var txtTempoEstados: TextView
    private lateinit var txtLeituras: TextView
    private lateinit var celulas: List<List<TextView>>
    private lateinit var graficos: List<Grafico>
    private var visual = "" // estado desenhado por último, para o crossfade

    // Atualiza também sem novas leituras, para notar quando o detector para de enviar.
    private val relogio = object : Runnable {
        override fun run() {
            atualizar()
            txtEstado.postDelayed(this, 5000)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(temaAtual().estilo)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txtConexao = findViewById(R.id.txtConexao)
        ladrilhoEstado = findViewById(R.id.ladrilhoEstado)
        icEstado = findViewById(R.id.icEstado)
        txtEstado = findViewById(R.id.txtEstado)
        txtMensagem = findViewById(R.id.txtMensagem)
        txtAtualizado = findViewById(R.id.txtAtualizado)
        btnConectar = findViewById(R.id.btnConectar)
        btnDesconectar = findViewById(R.id.btnDesconectar)
        barra = findViewById(R.id.barra)
        txtPeriodo = findViewById(R.id.txtPeriodo)
        txtInicio = findViewById(R.id.txtInicio)
        txtTempoEstados = findViewById(R.id.txtTempoEstados)
        txtLeituras = findViewById(R.id.txtLeituras)

        val ladrilhos = listOf(R.id.sensorMq2, R.id.sensorMq135, R.id.sensorTemp).map { findViewById<View>(it) }
        ladrilhos.forEachIndexed { i, v ->
            v.findViewById<TextView>(R.id.nome).text = sensores[i].nome
            v.findViewById<TextView>(R.id.tecnico).text = sensores[i].tecnico
            v.findViewById<TextView>(R.id.significado).text = sensores[i].significado
            v.findViewById<TextView>(R.id.unidade).text = sensores[i].unidade
        }
        valores = ladrilhos.map { it.findViewById(R.id.valor) }
        unidades = ladrilhos.map { it.findViewById(R.id.unidade) }
        significados = ladrilhos.map { it.findViewById(R.id.significado) }
        txtFim = findViewById(R.id.txtFim)
        graficos = listOf(R.id.graficoMq2, R.id.graficoMq135, R.id.graficoTemp).mapIndexed { i, id ->
            val v = findViewById<View>(id)
            v.findViewById<TextView>(R.id.nome).text = sensores[i].nome
            v.findViewById<TextView>(R.id.tecnico).text = sensores[i].tecnico
            v.findViewById(R.id.grafico)
        }
        celulas = montarTabela()

        btnConectar.setOnClickListener {
            val faltando = permissoes().filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
            if (faltando.isEmpty()) escolherDispositivo() else requestPermissions(faltando.toTypedArray(), 1)
        }
        btnDesconectar.setOnClickListener {
            stopService(Intent(this, MonitorService::class.java))
        }
        findViewById<View>(R.id.btnTema).setOnClickListener { escolherTema() }
    }

    private fun escolherTema() {
        val atual = temaAtual()
        val lado = (22 * resources.displayMetrics.density).toInt()
        // cada opção mostra o ladrilho do cabeçalho pintado no próprio tema
        val opcoes = TEMAS.map { t ->
            val amostra = ContextThemeWrapper(this, t.estilo).getDrawable(R.drawable.ladrilho_marca)!!.apply { setBounds(0, 0, lado, lado) }
            val alinhamento = if (Build.VERSION.SDK_INT >= 29) ImageSpan.ALIGN_CENTER else ImageSpan.ALIGN_BOTTOM
            SpannableString("   ${t.nome}").apply { setSpan(ImageSpan(amostra, alinhamento), 0, 1, 0) }
        }
        AlertDialog.Builder(this)
            .setTitle("Tema do app")
            .setSingleChoiceItems(opcoes.toTypedArray(), TEMAS.indexOf(atual)) { dialogo, i ->
                dialogo.dismiss()
                if (TEMAS[i] != atual) {
                    salvarTema(TEMAS[i])
                    recreate()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onResume() {
        super.onResume()
        Monitor.ouvinte = { runOnUiThread { atualizar() } }
        relogio.run()
    }

    override fun onPause() {
        super.onPause()
        Monitor.ouvinte = null
        txtEstado.removeCallbacks(relogio)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (Build.VERSION.SDK_INT >= 33 &&
            checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            Toast.makeText(this, "Sem permissão de notificação, os alertas não vão aparecer", Toast.LENGTH_LONG).show()
        }
        if (temPermissaoBluetooth()) {
            escolherDispositivo()
        } else {
            Toast.makeText(this, "Permita o Bluetooth para conectar ao detector", Toast.LENGTH_LONG).show()
        }
    }

    private fun permissoes(): List<String> = buildList {
        if (Build.VERSION.SDK_INT >= 31) add(Manifest.permission.BLUETOOTH_CONNECT)
        if (Build.VERSION.SDK_INT >= 33) add(Manifest.permission.POST_NOTIFICATIONS)
    }

    private fun temPermissaoBluetooth() = Build.VERSION.SDK_INT < 31 ||
        checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED

    private fun escolherDispositivo() {
        val adaptador = getSystemService(BluetoothManager::class.java).adapter
        // no debug o modo demonstração funciona mesmo sem Bluetooth (ex.: emulador)
        if (!BuildConfig.DEBUG) {
            if (adaptador == null) {
                Toast.makeText(this, "Este celular não tem Bluetooth", Toast.LENGTH_LONG).show()
                return
            }
            if (!adaptador.isEnabled) {
                startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                return
            }
        }
        val pareados = if (adaptador?.isEnabled == true) adaptador.bondedDevices.filter { it.name?.startsWith(PREFIXO_DETECTOR) == true } else emptyList()
        val enderecos = pareados.map { it.address } + if (BuildConfig.DEBUG) listOf(ENDERECO_DEMO) else emptyList()
        if (enderecos.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("Nenhum detector pareado")
                .setMessage("Pareie o detector nas configurações de Bluetooth do celular. Ele aparece como $PREFIXO_DETECTOR-01, com PIN 1234 ou 0000.")
                .setPositiveButton("Abrir Bluetooth") { _, _ -> startActivity(Intent(Settings.ACTION_BLUETOOTH_SETTINGS)) }
                .setNegativeButton("Agora não", null)
                .show()
            return
        }
        val linhas = pareados.map { mapOf("nome" to it.name, "detalhe" to it.address) } +
            if (BuildConfig.DEBUG) listOf(mapOf("nome" to "Modo demonstração", "detalhe" to "Dados simulados, sem Bluetooth")) else emptyList()
        val lista = SimpleAdapter(this, linhas, R.layout.item_detector, arrayOf("nome", "detalhe"), intArrayOf(R.id.nome, R.id.detalhe))
        AlertDialog.Builder(this)
            .setTitle("Escolha o detector")
            .setAdapter(lista) { _, i ->
                val intent = Intent(this, MonitorService::class.java)
                stopService(intent)
                startForegroundService(intent.putExtra(MonitorService.EXTRA_ENDERECO, enderecos[i]))
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun atualizar() {
        val leituras = Monitor.copia()
        val ultima = leituras.lastOrNull()
        txtConexao.text = Monitor.conexao
        val chave = when {
            !Monitor.ativo -> "desligado"
            !Monitor.conectado -> "sem_conexao"
            ultima == null -> "aguardando"
            System.currentTimeMillis() - ultima.em > 15_000 -> "parado"
            else -> "estado${ultima.estado}"
        }
        desenharEstado(chave, ultima)
        btnDesconectar.visibility = if (Monitor.ativo) View.VISIBLE else View.GONE

        // fora do ar, os valores são da última leitura: ficam apagados e dizem de quando são
        val aoVivo = chave.startsWith("estado")
        val horaUltima = ultima?.let { DateFormat.getTimeFormat(this).format(it.em) }
        unidades.forEach { it.visibility = if (ultima == null) View.GONE else View.VISIBLE }
        valores.forEach { it.setTextColor(getColor(if (aoVivo) R.color.tinta else R.color.tinta_fraca)) }
        significados.forEachIndexed { i, t ->
            t.text = if (aoVivo || horaUltima == null) sensores[i].significado else "Última leitura às $horaUltima"
        }
        txtFim.text = if (aoVivo || ultima == null) "agora" else "última leitura"
        if (ultima != null) {
            valores[0].text = "${ultima.mq2}"
            valores[1].text = "${ultima.mq135}"
            valores[2].text = "%.1f".format(ultima.temperatura)
        }

        val estados = leituras.map { it.estado }
        barra.estados = piorPorTrecho(estados, 12)
        if (leituras.isEmpty()) {
            txtPeriodo.text = "sem leituras"
            txtInicio.text = ""
            txtTempoEstados.text = "Ainda sem leituras nesta sessão."
            txtLeituras.text = ""
        } else {
            val tempo = ultima!!.em - leituras.first().em
            txtPeriodo.text = if (tempo >= 59 * 60_000) "última hora" else "últimos ${duracao(tempo)}"
            txtInicio.text = "há ${duracao(tempo)}"
            val contagem = IntArray(3).also { c -> estados.forEach { c[it]++ } }
            fun pct(e: Int) = Math.round(100f * contagem[e] / leituras.size)
            txtTempoEstados.text = "Normal ${pct(0)}%   ·   Atenção ${pct(1)}%   ·   Perigo ${pct(2)}%"
            txtLeituras.text = "${leituras.size} leituras"
        }
        barra.contentDescription = "Como esteve o ar: ${txtTempoEstados.text}"

        val series = listOf(
            leituras.map { it.mq2.toFloat() },
            leituras.map { it.mq135.toFloat() },
            leituras.map { it.temperatura },
        )
        series.forEachIndexed { i, v ->
            val formato = "%.${sensores[i].casas}f"
            val linha = if (v.isEmpty()) listOf("—", "—", "—")
                else listOf(formato.format(v.min()), formato.format(v.average()), formato.format(v.max()))
            linha.forEachIndexed { j, t -> celulas[i][j].text = t }
            graficos[i].definir(v, estados, sensores[i].casas, sensores[i].nome)
        }
    }

    private fun desenharEstado(chave: String, ultima: Leitura?) {
        val (titulo, mensagem) = when (chave) {
            "desligado" -> "Desconectado" to "Conecte o detector para receber os alertas no celular."
            "sem_conexao" -> "Sem conexão" to "Tentando falar com o detector. Enquanto isso, os alertas não chegam."
            "parado" -> "Sem leituras" to "O detector está conectado, mas parou de enviar leituras. Confira se ele está ligado."
            "aguardando" -> "Conectado" to "Esperando a primeira leitura do detector."
            "estado1" -> "Atenção" to "O ar da cozinha mudou.\n•  Abra as janelas.\n•  Confira se o fogão e o gás estão fechados."
            "estado2" -> "Perigo" to "Possível vazamento de gás ou incêndio.\n•  Feche o registro do gás.\n•  Não acione interruptores.\n•  Saia de casa.\nEmergência: ligue 193."
            else -> "Ar normal" to "Nenhum gás ou fumaça acima do normal."
        }
        val perigo = chave == "estado2"
        val cor = when (chave) {
            "estado0" -> getColor(R.color.normal)
            "estado1" -> getColor(R.color.atencao_vidrado)
            "estado2" -> getColor(R.color.perigo)
            "aguardando" -> corDoTema(R.attr.acao)
            else -> getColor(R.color.sem_conexao)
        }
        icEstado.setImageResource(
            when (chave) {
                "estado0" -> R.drawable.ic_normal
                "estado1" -> R.drawable.ic_atencao
                "estado2" -> R.drawable.ic_perigo
                "aguardando" -> R.drawable.ic_conectado
                else -> R.drawable.ic_sem_conexao
            },
        )
        icEstado.imageTintList = ColorStateList.valueOf(
            when (chave) {
                "estado2" -> Color.WHITE
                "estado1" -> getColor(R.color.atencao)
                else -> cor
            },
        )
        txtEstado.text = titulo
        txtMensagem.text = mensagem
        txtMensagem.gravity = if (chave == "estado1" || perigo) Gravity.START else Gravity.CENTER_HORIZONTAL
        txtAtualizado.text = ultima?.let { "Última leitura às ${DateFormat.getTimeFormat(this).format(it.em)}" } ?: ""
        txtAtualizado.visibility = if (ultima == null) View.GONE else View.VISIBLE
        btnConectar.visibility = if (chave == "desligado") View.VISIBLE else View.GONE

        // só o perigo vira ladrilho cheio; os outros estados ficam no branco com a borda na cor do estado
        ladrilhoEstado.background = if (perigo) ladrilho(cor, getColor(R.color.perigo_base), null)
            else ladrilho(getColor(R.color.azulejo), getColor(R.color.esmalte_base), cor)
        txtEstado.setTextColor(if (perigo) Color.WHITE else getColor(R.color.tinta))
        txtMensagem.setTextColor(if (perigo) Color.WHITE else getColor(R.color.tinta_media))
        txtAtualizado.setTextColor(if (perigo) 0xFFFFE1DD.toInt() else getColor(R.color.tinta_fraca))

        if (visual.isNotEmpty() && visual != chave) {
            ladrilhoEstado.alpha = 0.35f
            ladrilhoEstado.animate().alpha(1f).setDuration(280).setInterpolator(DecelerateInterpolator(2f)).start()
        }
        visual = chave
    }

    // Ladrilho desenhado em código para o estado: face, filete de esmalte na base e borda opcional.
    private fun ladrilho(face: Int, base: Int, borda: Int?): Drawable {
        val d = resources.displayMetrics.density
        fun forma(cor: Int) = GradientDrawable().apply {
            cornerRadius = 3 * d
            setColor(cor)
        }
        val frente = forma(face).apply { if (borda != null) setStroke((3 * d).toInt(), borda) }
        return LayerDrawable(arrayOf(forma(base), frente)).apply { setLayerInset(1, 0, 0, 0, (2 * d).toInt()) }
    }

    // Tabela do resumo: uma linha por sensor com mínimo, média e máximo.
    private fun montarTabela(): List<List<TextView>> {
        val tabela = findViewById<TableLayout>(R.id.tabela)
        val d = resources.displayMetrics.density
        tabela.dividerDrawable = GradientDrawable().apply {
            setColor(getColor(R.color.fio))
            setSize(0, maxOf(1, d.toInt()))
        }
        tabela.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        fun celula(texto: String, estilo: Int, inicio: Boolean = false) = TextView(this, null, 0, estilo).apply {
            text = texto
            fontFeatureSettings = "tnum"
            gravity = if (inicio) Gravity.START else Gravity.END
            setPadding(if (inicio) 0 else (12 * d).toInt(), (10 * d).toInt(), 0, (10 * d).toInt())
        }
        tabela.addView(TableRow(this).apply {
            listOf("", "mín", "média", "máx").forEachIndexed { i, t -> addView(celula(t, R.style.Texto_Tecnico, i == 0)) }
        })
        return sensores.map { s ->
            val linha = TableRow(this)
            linha.addView(celula(s.nome, R.style.Texto_Corpo, true).apply { setTextColor(getColor(R.color.tinta)) })
            List(3) { celula("—", R.style.Texto_Corpo).apply { setTextColor(getColor(R.color.tinta)) }.also { linha.addView(it) } }
                .also { tabela.addView(linha) }
        }
    }
}
