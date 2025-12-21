package com.tacosforchessur.hardwire.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tacosforchessur.hardwire.domain.models.Chord
import com.tacosforchessur.hardwire.domain.repository.ChordLibrary

class ChordChangerViewModel : ViewModel() {
    private val _practicePool = mutableStateListOf<Chord>()
    val practicePool: List<Chord> get() = _practicePool

    var currentChord by mutableStateOf<Chord?>(null)
    private set

    init {
        _practicePool.addAll(ChordLibrary.allChords)
        pickNextChord()
    }

    var beatsToChange by mutableStateOf(4)

    fun updatePracticePool(chords: List<Chord>) {
        _practicePool.clear()
        _practicePool.addAll(chords)
        if (currentChord == null) pickNextChord()
    }

    fun pickNextChord() {
        if (_practicePool.isEmpty()) return
        val next = if(_practicePool.size > 1) {
            _practicePool.filter { it != currentChord }.random()
        } else {
            _practicePool.first()
        }
        currentChord = next
    }

    fun toggleChordSelection(chord: Chord) {
        if (_practicePool.contains(chord)) {
            if (_practicePool.size > 1) {
                _practicePool.remove(chord)
                if (currentChord == chord) {
                    pickNextChord()
                }
            }
        } else {
            _practicePool.add(chord)
        }
    }
}