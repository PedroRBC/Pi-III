package com.pi3.detector

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

// Barra da última hora: uma faixa de ladrilhos assentados no rejunte, cada um vitrificado
// na pior cor de estado do seu trecho, com o mesmo filete de esmalte na base dos outros ladrilhos.
class BarraEstados(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    var estados: List<Int?> = emptyList()
        set(v) {
            field = v
            invalidate()
        }

    private val densidade = resources.displayMetrics.density
    private val pincel = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rejunte = context.getColor(R.color.rejunte)
    // face e base de cada ladrilho: vazio, normal, atenção, perigo
    private val faces = intArrayOf(R.color.azulejo, R.color.normal_vidrado, R.color.atencao_vidrado, R.color.perigo).map(context::getColor)
    private val bases = intArrayOf(R.color.esmalte_base, R.color.normal_base, R.color.atencao_base, R.color.perigo_base).map(context::getColor)

    override fun onDraw(canvas: Canvas) {
        if (estados.isEmpty()) return
        val junta = 3 * densidade
        val canto = 2 * densidade
        pincel.color = rejunte
        canvas.drawRoundRect(0f, 0f, width.toFloat(), height.toFloat(), canto, canto, pincel)
        val largura = (width - junta * (estados.size + 1)) / estados.size
        estados.forEachIndexed { i, e ->
            val k = if (e == null) 0 else e + 1
            val x = junta + i * (largura + junta)
            pincel.color = bases[k]
            canvas.drawRoundRect(x, junta, x + largura, height - junta, canto, canto, pincel)
            pincel.color = faces[k]
            canvas.drawRoundRect(x, junta, x + largura, height - junta - 2 * densidade, canto, canto, pincel)
        }
    }
}
