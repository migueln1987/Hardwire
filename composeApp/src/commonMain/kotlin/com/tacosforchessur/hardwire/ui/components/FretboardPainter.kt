package com.tacosforchessur.hardwire.ui.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.tacosforchessur.hardwire.core.FRETS
import com.tacosforchessur.hardwire.core.STRINGS

fun DrawScope.drawFretboard(
    fretSpacing: Float,
    baseFret: Int,
    lineColor: Color = Color.White,
    fretColor: Color
) {
    val columnWidth = size.width / STRINGS
    val halfColumn = columnWidth / 2f

    for (i in 0 until STRINGS) {
        val x = (i * columnWidth) + halfColumn
        drawLine(
            color = lineColor,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }

    val lastStringX = size.width - halfColumn

    for (i in 0..FRETS) {
        val y = i * fretSpacing
        val isGuitarNut = (i == 0 && baseFret == 1)
        val stroke = if (isGuitarNut) 10f else 2f
        drawLine(
            color = fretColor,
            start = Offset(halfColumn, y),
            end = Offset(lastStringX, y),
            strokeWidth = stroke
        )
    }
}

fun DrawScope.drawInlays(
    inlays: List<Int>,
    baseFret: Int,
    fretSpacing: Float,
    fretBoardCenter: Float,
    columnWith: Float
) {
    inlays.forEach { inlayFret ->
        val rowIndex = inlayFret - baseFret
        if (rowIndex in 0 until FRETS) {
            val y = (rowIndex * fretSpacing) + (fretSpacing / 2f)
            if (inlayFret == 12) {
                drawCircle(Color.Black, 6f, Offset(fretBoardCenter - columnWith, y))
                drawCircle(Color.Black, 6f, Offset(fretBoardCenter + columnWith, y))
            } else {
                drawCircle(Color.Black, 6f, Offset(fretBoardCenter, y))
            }
        }
    }
}