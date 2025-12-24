    package com.tacosforchessur.hardwire.ui.screens.metronome.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tacosforchessur.hardwire.ElectricCyan
import com.tacosforchessur.hardwire.MaterialGreen
import com.tacosforchessur.hardwire.MaterialRed
import com.tacosforchessur.hardwire.ui.components.IntegerSpinner
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeControlPanel(
    bpm: Int = 0,
    bpmOptions: List<Int> = listOf(),
    measureOptions: List<Int> = listOf(),
    beatsPerMeasure: Int = 0,
    onUpdateBpm: (Int) -> Unit = {},
    onUpdateBeatsPerMeasure: (Int) -> Unit = {},
    onToggleMetronome: () -> Unit = {},
    isRunning: Boolean = true,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        IntegerSpinner(
            label = "BPM",
            value = bpm,
            options = bpmOptions,
            onValueChange = { onUpdateBpm(it) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        IntegerSpinner(
            label = "Beats",
            value = beatsPerMeasure,
            options = measureOptions,
            onValueChange = { onUpdateBeatsPerMeasure(it) },
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        )

        Slider(
            modifier = Modifier
                .fillMaxWidth()
                .height(26.dp),
            value = bpm.toFloat(),
            onValueChange = { newValue ->
                onUpdateBpm(newValue.toInt())
            },
            thumb = {
                Surface(
                    modifier = Modifier.size(26.dp),
                    shape = CircleShape,
                    color = Color.White,
                    tonalElevation = 0.dp,
                    shadowElevation = 0.dp,
                ) {}
            },
            colors = SliderDefaults.colors(
                thumbColor = Color.Transparent,
                activeTrackColor = ElectricCyan,
                inactiveTrackColor = Color.DarkGray,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent
            ),
            valueRange = 40f..220f
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 80.dp)
                .padding(horizontal = 75.dp, vertical = 25.dp),
            onClick = {
                onToggleMetronome()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isRunning) MaterialRed else MaterialGreen
            )
        ) {
            Text(
                text = if (isRunning) "Stop" else "Start",
                color = Color.White,
                style = TextStyle(fontSize = 20.sp)
            )
        }
    }
}

@Composable
@Preview
fun MetronomeControlPanelPreview() {
    MetronomeControlPanel()
}
