package com.tacosforchessur.hardwire.domain.repository

import com.tacosforchessur.hardwire.domain.models.Barre
import com.tacosforchessur.hardwire.domain.models.Chord
import com.tacosforchessur.hardwire.domain.models.Finger

object ChordLibrary {
    val G_Major = Chord(
        name = "G Major",
        fingers = listOf(
            Finger(string = 1, fret = 3, label = "1"),
            Finger(string = 2, fret = 2, label = "2"),
            Finger(string = 6, fret = 3, label = "3")
        )
    )

    val C_Major = Chord(
        name = "C Major",
        fingers = listOf(
            Finger(string = 2, fret = 3, label = "1"),
            Finger(string = 4, fret = 2, label = "2"),
            Finger(string = 5, fret = 1, label = "3")
        )
    )

    val A_Major = Chord(
        name = "A Major",
        fingers = listOf(
            Finger(string = 3, fret = 2, label = "2"),
            Finger(string = 4, fret = 2, label = "3"),
            Finger(string = 5, fret = 2, label = "4")
        ),
        mutedString = listOf(1)
    )

    val B_Minor = Chord(
        name = "B Minor",
        baseFret = 2,
        barre = Barre(fret = 1, startString = 2, endString = 6),
        fingers = listOf(
            Finger(string = 2, fret = 1, label = "1"), // Part of barre
            Finger(string = 3, fret = 3, label = "3"),
            Finger(string = 4, fret = 3, label = "4"),
            Finger(string = 5, fret = 2, label = "2")
        ),
        mutedString = listOf(1)
    )

    val D_Major_High = Chord(
        name = "D (Pos V)",
        baseFret = 5,
        fingers = listOf(
            Finger(string = 2, fret = 1, label = "1"),
            Finger(string = 3, fret = 3, label = "2"),
            Finger(string = 4, fret = 3, label = "3"),
            Finger(string = 5, fret = 3, label = "4")
        ),
        mutedString = listOf(1, 6)
    )

    val E_Minor_7 = Chord(
        name = "Em7",
        fingers = listOf(
            Finger(string = 2, fret = 2, label = "2")
        )
    )

    val allChords = listOf(G_Major, C_Major, A_Major, B_Minor, D_Major_High, E_Minor_7)
}