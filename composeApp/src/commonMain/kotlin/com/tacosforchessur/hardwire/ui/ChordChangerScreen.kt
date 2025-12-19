package com.tacosforchessur.hardwire.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    chord: Chord = ChordLibrary.A_Major,
    modifier: Modifier = Modifier,
    baseFret: Int = 1,
    inlays: List<Int> = listOf(3, 5, 7, 9, 12, 15)
) {
    Column {
        Text(
            text = chord.name,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.ExtraBold
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            val tuning = listOf("E", "A", "D", "G", "B", "E")
            repeat(STRINGS) { i ->
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = tuning[i], fontWeight = FontWeight.Bold)
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            repeat(STRINGS) { i ->
                val stringNum = i + 1
                Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    val isMuted = stringNum in chord.mutedString
                    val isOpen = !chord.fingers.any{ it.string == stringNum}
                    if (isMuted) Text(text = "x") else if (isOpen) Text(text = "o")
                }
            }
        }
        Box(
            modifier = modifier
                .fillMaxSize()
                .weight(1f)
                .drawBehind {
                    val fretSpacing = size.height / FRETS
                    val fretBoardCenter = size.width / 2f
                    val columnWith = size.width / STRINGS
                    drawFretboard(fretSpacing, baseFret)

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
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(FRETS) { rowIndex ->
                    val currentFret = baseFret + rowIndex
                    Row(modifier = Modifier.weight(1f)) {
                        repeat(STRINGS) { colIndex ->
                            val stringNum = colIndex + 1
                            val finger = chord.fingers.find { it.string == stringNum && it.fret == currentFret }
                            println("Finger: $finger")
                            BoxWithConstraints(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (finger != null) {
                                    val displayLabel = if (finger.label.isNullOrEmpty()) {
                                        if(finger.fret > 0) finger.fret.toString() else ""
                                    } else {
                                        finger.label
                                    }
                                    FingerMarker(
                                        label = displayLabel,
                                        modifier = Modifier.size(maxWidth * 0.7f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


fun DrawScope.drawFretboard(fretSpacing: Float, baseFret: Int) {
    val columnWidth = size.width / STRINGS
    val halfColumn = columnWidth / 2f

    for (i in 0 until STRINGS) {
        val x = (i * columnWidth) + halfColumn
        drawLine(
            color = Color.Black,
            start = Offset(x, 0f),
            end = Offset(x, size.height),
            strokeWidth = 1.dp.toPx()
        )
    }

    val lastStringX = size.width - halfColumn

    for (i in 0..FRETS) {
        val y = i * fretSpacing
        val isNut = (i == 0 && baseFret == 1)
        drawLine(
            color = Color.Black,
            start = Offset(halfColumn, y),
            end = Offset(lastStringX, y),
            strokeWidth = if (isNut) 10f else 2f
        )
    }
}

@Composable
fun InlayDot(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(12.dp)
            .background(
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = CircleShape
            )
    )
}

@Composable
fun FingerMarker(
    label: String,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color.Black, shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        val size = if (maxHeight > 0.dp) maxHeight else 30.dp
        val fontSize = with(LocalDensity.current) {
            (size.toPx() * 0.5f).toSp()
        }
        if(label.isNotEmpty()) {
            Text(
                text = label,
                color = Color.White,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                softWrap = false
            )
        }
    }
}

@Composable
@Preview
fun ChordDiagramPreview() {
    Scaffold {
        ChordDiagram()
    }
}

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
}
