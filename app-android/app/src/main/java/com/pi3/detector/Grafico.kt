package com.pi3.detector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

// Gráfico de um sensor: um traço só, escala entre o mínimo e o máximo da sessão,
// e faixas ao fundo marcando quando o estado foi atenção ou perigo.
class Grafico(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    private var valores: List<Float> = emptyList()
    private var estados: List<Int> = emptyList()
    private var casas = 0

    fun definir(valores: List<Float>, estados: List<Int>, casas: Int, nome: String) {
        this.valores = valores
        this.estados = estados
        this.casas = casas
        contentDescription = if (valores.isEmpty()) "$nome: sem leituras ainda"
            else "$nome: mínimo ${numero(valores.min())}, máximo ${numero(valores.max())}, agora ${numero(valores.last())}"
        invalidate()
    }

    private val densidade = resources.displayMetrics.density
    private val linha = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 2 * densidade
        strokeJoin = Paint.Join.ROUND
        color = context.corDoTema(R.attr.acao)
    }
    private val ponto = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = context.corDoTema(R.attr.acao) }
    private val fio = Paint().apply { color = context.getColor(R.color.fio) }
    private val faixa = Paint()
    private val corAtencao = context.getColor(R.color.faixa_atencao)
    private val corPerigo = context.getColor(R.color.faixa_perigo)
    private val texto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = context.getColor(R.color.tinta_fraca)
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics)
        fontFeatureSettings = "tnum"
    }
    private val caminho = Path()

    private fun numero(v: Float) = "%.${casas}f".format(v)

    override fun onDraw(canvas: Canvas) {
        val topo = texto.textSize + 8 * densidade
        val base = height - texto.textSize - 8 * densidade
        val direita = width - 5 * densidade
        canvas.drawRect(0f, topo, width.toFloat(), topo + densidade, fio)
        canvas.drawRect(0f, base - densidade, width.toFloat(), base, fio)
        if (valores.size < 2) {
            canvas.drawText("Sem leituras ainda", 0f, (topo + base) / 2, texto)
            return
        }
        val passo = direita / (valores.size - 1)
        var i = 0
        while (i < estados.size) {
            val e = estados[i]
            var j = i
            while (j + 1 < estados.size && estados[j + 1] == e) j++
            if (e > 0) {
                faixa.color = if (e == 2) corPerigo else corAtencao
                canvas.drawRect(maxOf(0f, (i - 0.5f) * passo), topo + densidade, minOf(width.toFloat(), (j + 0.5f) * passo), base - densidade, faixa)
            }
            i = j + 1
        }
        val min = valores.min()
        val max = valores.max()
        val amplitude = if (max > min) max - min else 1f
        val margem = 6 * densidade
        val altura = base - topo - 2 * margem
        fun y(v: Float) = base - margem - (v - min) / amplitude * altura
        caminho.reset()
        valores.forEachIndexed { k, v -> if (k == 0) caminho.moveTo(0f, y(v)) else caminho.lineTo(k * passo, y(v)) }
        canvas.drawPath(caminho, linha)
        canvas.drawCircle(direita, y(valores.last()), 3.5f * densidade, ponto)

        canvas.drawText("máx ${numero(max)}", 0f, texto.textSize, texto)
        canvas.drawText("mín ${numero(min)}", 0f, height - texto.descent(), texto)
        val agora = "agora ${numero(valores.last())}"
        canvas.drawText(agora, width - texto.measureText(agora), height - texto.descent(), texto)
        // legenda das faixas, só quando aparecem
        var x = width.toFloat()
        for ((e, nome) in listOf(2 to "perigo", 1 to "atenção")) {
            if (e !in estados) continue
            x -= texto.measureText(nome)
            canvas.drawText(nome, x, texto.textSize, texto)
            val lado = 10 * densidade
            x -= lado + 4 * densidade
            faixa.color = if (e == 2) corPerigo else corAtencao
            canvas.drawRect(x, texto.textSize - lado + densidade, x + lado, texto.textSize + densidade, faixa)
            x -= 12 * densidade
        }
    }
}
