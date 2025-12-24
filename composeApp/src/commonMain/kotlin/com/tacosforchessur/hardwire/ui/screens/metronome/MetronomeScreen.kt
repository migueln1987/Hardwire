package com.tacosforchessur.hardwire.ui.screens.metronome

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tacosforchessur.hardwire.viewmodel.MetronomeViewModel
import kotlinx.coroutines.delay
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tacosforchessur.hardwire.ui.components.IntegerSpinner
import com.tacosforchessur.hardwire.ui.screens.metronome.components.HybridBeatCounter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen(viewModel: MetronomeViewModel = viewModel()) {
    val bpm by viewModel.bpm.collectAsStateWithLifecycle()
    val isRunning by viewModel.isTicking.collectAsStateWithLifecycle()
    val beatsPerMeasure by viewModel.beatsPerMeasure.collectAsStateWithLifecycle()
    val currentBeat = viewModel.currentBeat

    Scaffold(
        topBar = {
        TopAppBar(
            title = { Text(text = "Metronome") },
        )
    }) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HybridBeatCounter(
                currentBeat = currentBeat,
                totalBeats = beatsPerMeasure,
                isRunning = isRunning
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IntegerSpinner(
                    label = "BPM",
                    value = bpm,
                    options = viewModel.bpmOptions,
                    onValueChange = { viewModel.updateBpm(it) },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )

                IntegerSpinner(
                    label = "Beats",
                    value = viewModel.beatsPerMeasure.value,
                    options = viewModel.measureOptions,
                    onValueChange = { viewModel.updateBeatsPerMeasure(it) },
                    modifier = Modifier.weight(1f).padding(8.dp)
                )
            }

            Slider(
                value = bpm.toFloat(),
                onValueChange = {
                    viewModel.updateBpm(bpm)
                },
                valueRange = 40f..220f
            )

            Button(
                onClick = {
                    viewModel.toggleMetronome()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red else Color.Green
                )
            ) {
                Text(text = if (isRunning) "Stop" else "Start")
            }
        }

    }
}

@Composable
fun MetronomeVisualizer(viewModel: MetronomeViewModel) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.ticketEvent.collect {
            isVisible = true
            delay(100)
            isVisible = false
        }
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(
                color = if (isVisible) Color.Cyan else Color.Gray,
                shape = CircleShape
            )
    )
}