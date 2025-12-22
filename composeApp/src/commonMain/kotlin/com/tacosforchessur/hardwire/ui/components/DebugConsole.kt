package com.tacosforchessur.hardwire.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tacosforchessur.hardwire.core.logging.LogLevel
import com.tacosforchessur.hardwire.core.logging.Logger

@Composable
fun BoxScope.DebugConsole(
    isVisible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it }),
        modifier = modifier.fillMaxWidth(),
        label = "",
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight(0.4f)
                .background(Color.Black.copy(0.8f))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Terminal", color = Color.White, fontSize = 12.sp)
                Text(
                    text = "Clear logs",
                    color = Color.Blue,
                    modifier = Modifier.clickable { Logger.clearLogs() },
                    fontSize = 12.sp
                )
                Text(
                    text = "[X] Close",
                    color = Color.Red,
                    modifier = Modifier.clickable { onDismissRequest() },
                    fontSize = 12.sp
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f).padding(8.dp),
                reverseLayout = true,
                verticalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.Bottom)
            ) {
                items(Logger.logs.asReversed()) { entry ->
                    val textColor = when (entry.level) {
                        LogLevel.ERROR -> Color.Red
                        LogLevel.DEBUG -> Color.Green
                        LogLevel.INFO -> Color.Yellow
                    }

                    Text(
                        text = "[${entry.timeStamp}] ${entry.level.name.first()} | ${entry.message}",
                        color = textColor,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}