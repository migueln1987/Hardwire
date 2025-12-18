package com.tacosforchessur.hardwire.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tacosforchessur.hardwire.viewmodel.MetronomeViewModel
import kotlinx.coroutines.delay
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MetronomeScreen(initialBpm: Int = 120, viewModel: MetronomeViewModel = viewModel()) {
    var bpm by remember { mutableStateOf(initialBpm) }
    val isRunning by viewModel.isTicking.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        MetronomeVisualizer(viewModel)

        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Temp: $bpm", style = MaterialTheme.typography.headlineMedium)

        Slider(
            value = bpm.toFloat(),
            onValueChange = {
                bpm = it.toInt()
                viewModel.updateBpm(bpm)
            },
            valueRange = 40f..220f
        )

        Button(
            onClick = {
                println("UI: Button Clicked!")
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