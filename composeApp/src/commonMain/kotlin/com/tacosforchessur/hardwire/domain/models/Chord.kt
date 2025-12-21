package com.tacosforchessur.hardwire.domain.models

data class Finger(
    val string: Int,
    val fret: Int,
    val label: String? = null
)

data class Barre(
    val fret: Int,
    val startString: Int,
    val endString: Int,
)

data class Chord(
    val name: String,
    val fingers: List<Finger>,
    val mutedString: List<Int> = emptyList(),
    val baseFret: Int = 1,
    val barre: Barre? = null
)