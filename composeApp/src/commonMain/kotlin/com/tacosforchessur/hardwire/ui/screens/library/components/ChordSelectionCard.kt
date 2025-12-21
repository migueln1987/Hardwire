package com.tacosforchessur.hardwire.ui.screens.library.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tacosforchessur.hardwire.domain.models.Chord
import com.tacosforchessur.hardwire.domain.repository.ChordLibrary
import com.tacosforchessur.hardwire.ui.components.ChordDiagram
import org.jetbrains.compose.ui.tooling.preview.Preview

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