@file:OptIn(ExperimentalMaterial3Api::class)

package com.tacosforchessur.hardwire.ui.screens.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tacosforchessur.hardwire.viewmodel.ChordChangerViewModel
import androidx.compose.foundation.lazy.grid.items
import com.tacosforchessur.hardwire.domain.repository.ChordLibrary
import com.tacosforchessur.hardwire.ui.screens.library.components.ChordSelectionCard


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