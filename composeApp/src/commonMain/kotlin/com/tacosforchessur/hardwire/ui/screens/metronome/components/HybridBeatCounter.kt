package com.tacosforchessur.hardwire.ui.screens.metronome.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HybridBeatCounter(
    currentBeat: Int,
    totalBeats: Int,
    isRunning: Boolean
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(24.dp),
    ) {
        for (i in 1..totalBeats) {
            val isActive = i == currentBeat && isRunning

            val size by animateDpAsState(
                targetValue = if (isActive) 32.dp else 20.dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
            )
            Box(
                modifier = Modifier
                    .size(size)
                    .padding(4.dp)
                    .background(
                        color = when {
                            isActive && i == 1 -> Color.Red
                            isActive -> Color.Cyan
                            else -> Color.Gray.copy(alpha = 0.5f)
                        },
                        shape = CircleShape
                    )
            )
        }
    }
}