@file:OptIn(ExperimentalMaterial3Api::class)

package com.tacosforchessur.hardwire.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.tacosforchessur.hardwire.viewmodel.ChordChangerViewModel
import com.tacosforchessur.hardwire.viewmodel.MetronomeViewModel

@Composable
fun ChordChangerScreen(
    metronomeVm: MetronomeViewModel,
    chordVm: ChordChangerViewModel,
    onNavigateToMetronome: (Int) -> Unit,
    onNavigateToLibrary: () -> Unit,
) {
    val currentBeat = metronomeVm.currentBeat
    val isTicking by metronomeVm.isTicking.collectAsState()

    LaunchedEffect(Unit) {
        metronomeVm.ticketEvent.collect { beat ->
            if (beat == 1) {
                chordVm.pickNextChord()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chord Practice") },
                actions = {
                    IconButton(onClick = { metronomeVm.toggleMetronome() }) {
                        Text(if (isTicking) "⏸" else "▶")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
            Column(modifier = Modifier.weight(1f).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentBeat.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    color = if (currentBeat == 1) Color.Red else Color.Green
                )
                chordVm.currentChord?.let { chord ->
                    println("chord: $chord")
                    Column {
                        Text(
                            text = chord.name,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    ChordDiagram(chord = chord)
                }
            }
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                OutlinedButton(
                    onClick = { onNavigateToMetronome(metronomeVm.bpm.value) },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text("Adjust Metronome & BPM")
                }
                OutlinedButton(
                    onClick = { onNavigateToLibrary() },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                ) {
                    Text("Choose chords to practice")
                }
            }
        }
    }
}

const val STRINGS = 6
const val FRETS = 5

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
                                    val markerSize = if (maxWidth < maxHeight) maxWidth * 0.7f else maxHeight * 0.7f
                                    val displayLabel = if (finger.label.isNullOrEmpty()) {
                                        if (finger.fret > 0) finger.fret.toString() else ""
                                    } else {
                                        finger.label
                                    }
                                    FingerMarker(
                                        label = displayLabel,
                                        modifier = Modifier.size(markerSize)
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
        val isGuitarNut = (i == 0 && baseFret == 1)
        val stroke = if (isGuitarNut) 10f else 2f
        drawLine(
            color = Color.Black,
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
        if (label.isNotEmpty()) {
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

@Composable
fun ChordLibraryScreen(
    chordVm: ChordChangerViewModel,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Practice Library") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text(text = "<-")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(paddingValues = paddingValues).fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(ChordLibrary.allChords) { chord ->
                val isSelected = chordVm.practicePool.contains(chord)
                ChordSelectionCard(
                    chord = chord,
                    isSelected = isSelected,
                    onToggle = { chordVm.toggleChordSelection(chord)}
                )
            }
        }
    }
}

@Composable
fun ChordSelectionCard(
    chord: Chord,
    isSelected: Boolean,
    onToggle: () -> Unit,
) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        else null,
        modifier = Modifier
            .padding(4.dp)
            .height(180.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = chord.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            ChordDiagram(
                chord = chord,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChordSelectionCardPreview() {
    MaterialTheme {
        Column {
            ChordSelectionCard(
                chord = ChordLibrary.C_Major,
                isSelected = true,
                onToggle = { }
            )
            ChordSelectionCard(
                chord = ChordLibrary.A_Major,
                isSelected = false,
                onToggle = { }
            )
        }
    }
}