package com.tacosforchessur.hardwire.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChordChangerScreen() {
    Scaffold {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxHeight(0.5f).padding(16.dp), contentAlignment = Alignment.Center) {
                ChordDiagram()
            }
            Box(modifier = Modifier.fillMaxHeight(0.5f).padding(16.dp), contentAlignment = Alignment.Center) {
                Text("Control Panel")
            }
        }
    }
}

@Composable
@Preview
fun ChordChangerScreenPreview() {
    ChordChangerScreen()
}

const val STRINGS = 6
const val FRETS = 5

@Composable
fun ChordDiagram(
    modifier: Modifier = Modifier,
    baseFret: Int = 1,
    inlays: List<Int> = listOf(3, 5, 7, 9, 12, 15)
) {
    Box(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)
        .border(width = 2.dp, color = Color.Black)
        .drawBehind {
            val stringSpacing = size.width / (STRINGS - 1)
            val fretSpacing = size.height / FRETS
            drawFretboard(stringSpacing, fretSpacing)

        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            repeat(FRETS) { rowIndex ->
                val currentFret = baseFret + rowIndex
                Box(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    when (currentFret) {
                        12 -> {
                            Row(modifier = Modifier.fillMaxWidth(0.6f),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                Text("Fret $currentFret", color = Color.Red)
                                Text("Fret $currentFret", color = Color.Red)
                            }
                        }
                        in inlays -> {
                            Text("Fret $currentFret", color = Color.Blue)
                        }
                    }
                }
            }
        }
    }
}


fun DrawScope.drawFretboard(stringSpacing: Float, fretSpacing: Float) {
    for (i in 0..FRETS) {
        val y = i * fretSpacing
        drawLine(
            color = Color.Black,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = if (i == 0) 5.dp.toPx() else 1.dp.toPx()
        )
    }

    for (i in 0 until STRINGS) {
        drawLine(
            color = Color.Black,
            start = Offset(i * stringSpacing, 0f),
            end = Offset(i * stringSpacing, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }
}

@Composable
@Preview
fun ChordDiagramPreview() {
    Scaffold {
        ChordDiagram()
    }
}
