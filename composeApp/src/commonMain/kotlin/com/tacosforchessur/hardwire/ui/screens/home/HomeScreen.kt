package com.tacosforchessur.hardwire.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun HomeScreen(onNavigateToMetronome: (Int) -> Unit = {}, onNavigateToChordChanger: () -> Unit = {}) {
    Scaffold {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column {
                Button(onClick = { onNavigateToMetronome(120) }) {
                    Text("Open Metronome")
                }
                Button(onClick = { onNavigateToChordChanger() }) {
                    Text(text = "Chord Changer")
                }
            }
        }
    }
}

@Composable
@Preview
fun HomeScreenPreview() {
    HomeScreen()
}