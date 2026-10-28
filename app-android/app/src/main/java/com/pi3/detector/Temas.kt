package com.pi3.detector

import android.content.Context
import android.util.TypedValue

// Temas de cor: muda só a cor da marca. Verde, âmbar e vermelho ficam com os estados em todos.
class Tema(val chave: String, val nome: String, val estilo: Int)

val TEMAS = listOf(
    Tema("roxo", "Roxo e cinza", R.style.Tema),
    Tema("azul", "Azul azulejo", R.style.Tema_Azul),
    Tema("petroleo", "Petróleo", R.style.Tema_Petroleo),
    Tema("grafite", "Grafite", R.style.Tema_Grafite),
)

private fun Context.preferencias() = getSharedPreferences("detector", Context.MODE_PRIVATE)

fun Context.temaAtual(): Tema = preferencias().getString("tema", null).let { c -> TEMAS.find { it.chave == c } } ?: TEMAS[0]

fun Context.salvarTema(tema: Tema) = preferencias().edit().putString("tema", tema.chave).apply()

// Cor de um atributo do tema, ex.: R.attr.marca.
fun Context.corDoTema(attr: Int): Int = TypedValue().also { theme.resolveAttribute(attr, it, true) }.data
