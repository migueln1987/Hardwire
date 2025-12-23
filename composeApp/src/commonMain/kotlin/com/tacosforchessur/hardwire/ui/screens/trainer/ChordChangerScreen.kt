@file:OptIn(ExperimentalMaterial3Api::class)

package com.tacosforchessur.hardwire.ui.screens.trainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tacosforchessur.hardwire.domain.models.Chord
import com.tacosforchessur.hardwire.domain.repository.ChordLibrary
import com.tacosforchessur.hardwire.ui.components.AdaptiveScreen
import com.tacosforchessur.hardwire.ui.components.ChordDiagram
import com.tacosforchessur.hardwire.viewmodel.ChordChangerViewModel
import com.tacosforchessur.hardwire.viewmodel.MetronomeViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview

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
    ChordChangerContent(
        isTicking = isTicking,
        currentBeat = currentBeat,
        currentChord = chordVm.currentChord,
        onToggle = { metronomeVm.toggleMetronome() },
        onNavigateToMetronome = { onNavigateToMetronome(metronomeVm.bpm.value) },
        onNavigateToLibrary = onNavigateToLibrary
    )
}

@Composable
fun ChordChangerContent(
    isTicking: Boolean,
    currentBeat: Int,
    currentChord: Chord?,
    onToggle: () -> Unit,
    onNavigateToMetronome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chord Practice") },
                actions = {
                    IconButton(onClick = onToggle) {
                        Text(if (isTicking) "⏸" else "▶")
                    }
                }
            )
        }
    ) { innerPadding ->
        AdaptiveScreen(
            tabletView = {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ButtonPanel(
                        onNavigateToMetronome = onNavigateToMetronome,
                        onNavigateToLibrary = onNavigateToLibrary,
                        modifier = Modifier.weight(1f).fillMaxHeight()
                    )
                    ChordDisplay(
                        currentBeat = currentBeat,
                        currentChord = currentChord,
                        Modifier.weight(3f).fillMaxHeight()
                    )
                }
            },
            phoneView = {
                Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp)) {
                    ChordDisplay(
                        currentBeat = currentBeat,
                        currentChord = currentChord,
                        Modifier.weight(1f).fillMaxWidth()
                    )
                    ButtonPanel(
                        onNavigateToMetronome = onNavigateToMetronome,
                        onNavigateToLibrary = onNavigateToLibrary
                    )
                }
            }

        )
    }
}

@Composable
fun ButtonPanel(
    onNavigateToMetronome: () -> Unit,
    onNavigateToLibrary: () -> Unit,
    modifier: Modifier = Modifier
) {
        Column(
            modifier = modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            OutlinedButton(
                onClick = { onNavigateToMetronome() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Adjust Metronome & BPM")
            }
            OutlinedButton(
                onClick = onNavigateToLibrary,
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Text("Choose chords to practice")
            }
    }
}

@Composable
fun ChordDisplay(
    currentBeat: Int,
    currentChord: Chord?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = currentBeat.toString(),
            style = MaterialTheme.typography.displayLarge,
            color = if (currentBeat == 1) Color.Red else Color.Green
        )
        currentChord?.let { chord ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = chord.name,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Box(
                    modifier = Modifier.weight(1f, fill = false),
                    contentAlignment = Alignment.Center
                ) {
                    ChordDiagram(chord = chord)
                }
            }
        }
    }
}

@Composable
@Preview
fun ChordChangerContentPreview() {
    ChordChangerContent(
        isTicking = false,
        currentBeat = 1,
        currentChord = ChordLibrary.G_Major,
        onToggle = {},
        onNavigateToLibrary = {},
        onNavigateToMetronome = {}
    )
}

@Preview(widthDp = 1024, heightDp = 768)
@Composable
fun ChordChangerLandscapePreview() {
        ChordChangerContent(
            isTicking = false,
            currentBeat = 1,
            currentChord = ChordLibrary.G_Major,
            onToggle = {},
            onNavigateToMetronome = {},
            onNavigateToLibrary = {}
        )
}