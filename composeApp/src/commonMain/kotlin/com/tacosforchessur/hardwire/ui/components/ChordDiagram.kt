package com.tacosforchessur.hardwire.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tacosforchessur.hardwire.core.FRETS
import com.tacosforchessur.hardwire.core.STRINGS
import com.tacosforchessur.hardwire.domain.models.Chord
import com.tacosforchessur.hardwire.domain.repository.ChordLibrary
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChordDiagram(
    chord: Chord = ChordLibrary.A_Major,
    modifier: Modifier = Modifier,
    inlays: List<Int> = listOf(3, 5, 7, 9, 12, 15)
) {
    Column {
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
                    val isOpen = !chord.fingers.any { it.string == stringNum }
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
                    drawFretboard(fretSpacing, chord.baseFret)
                    drawInlays(
                        inlays = inlays,
                        baseFret = chord.baseFret,
                        fretSpacing = fretSpacing,
                        fretBoardCenter = fretBoardCenter,
                        columnWith = columnWith
                    )
                }) {
            Column(modifier = Modifier.fillMaxSize()) {
                repeat(FRETS) { rowIndex ->
                    val currentFret = chord.baseFret + rowIndex
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        if (chord.baseFret > 1) {
                            Text(
                                text = "${chord.baseFret + rowIndex}",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Row(modifier = Modifier.weight(1f)) {
                        repeat(STRINGS) { colIndex ->
                            val stringNum = colIndex + 1
                            val finger = chord.fingers.find { it.string == stringNum && it.fret == currentFret }
                            BoxWithConstraints(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                                contentAlignment = Alignment.Center
                            ) {
                                if (finger != null) {
                                    val displayLabel = if (finger.label.isNullOrEmpty()) {
                                        if (finger.fret > 0) finger.fret.toString() else ""
                                    } else {
                                        finger.label
                                    }
                                    FingerMarker(
                                        label = displayLabel,
                                        modifier = Modifier.size(minOf(maxWidth, maxHeight) * 0.7f)
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

@Composable
@Preview
fun ChordDiagramPreview() {
    Scaffold {
        ChordDiagram()
    }
}