@file:OptIn(ExperimentalMaterial3Api::class)

package com.tacosforchessur.hardwire.ui.screens.trainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.tacosforchessur.hardwire.ui.components.ChordDiagram
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
