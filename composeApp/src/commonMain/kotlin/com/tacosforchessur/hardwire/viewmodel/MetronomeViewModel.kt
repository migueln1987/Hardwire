package com.tacosforchessur.hardwire.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tacosforchessur.hardwire.MetronomeEngine
import com.tacosforchessur.hardwire.Res
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MetronomeViewModel : ViewModel() {

    private val engine = MetronomeEngine()

    private val _tickEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val ticketEvent = _tickEvent.asSharedFlow()

    private val _bpm = MutableStateFlow(120)
    val bpm = _bpm.asStateFlow()

    private val _isTicking = MutableStateFlow(false)
    val isTicking = _isTicking.asStateFlow()

    var currentBeat by mutableStateOf(1)
    var beatsPerMeasure = 4 //TODO: Make this adjustable

    init {
        engine.onTick = {
            val isAccent = (currentBeat == 1)
            engine.playTick(isAccent)
            currentBeat = if (currentBeat >= beatsPerMeasure) 1 else currentBeat + 1
            _tickEvent.tryEmit(currentBeat)
        }
    }

    fun toggleMetronome() {
        if (_isTicking.value) {
            engine.stop()
        } else {
            engine.setBpm(_bpm.value)
            engine.start()
        }
        _isTicking.value = !_isTicking.value
    }

    fun updateBpm(newBpm: Int) {
        _bpm.value = newBpm
        engine.setBpm(newBpm)
    }

    override fun onCleared() {
        engine.stop()
        super.onCleared()
    }

}