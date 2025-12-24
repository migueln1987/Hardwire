package com.tacosforchessur.hardwire.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun FingerMarker(
    label: String,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(
        modifier = modifier
            .aspectRatio(1f)
            .background(Color.Gray, shape = CircleShape),
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
fun FingerMarkerPreview() {
    FingerMarker(
        label = "1"
    )
}