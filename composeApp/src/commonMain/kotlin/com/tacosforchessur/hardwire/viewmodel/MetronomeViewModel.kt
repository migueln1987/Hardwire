package com.tacosforchessur.hardwire.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.tacosforchessur.hardwire.MetronomeEngine
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class MetronomeViewModel : ViewModel() {

    private val engine = MetronomeEngine()

    private val _tickEvent = MutableSharedFlow<Int>(extraBufferCapacity = 1)
    val ticketEvent = _tickEvent.asSharedFlow()

    private val _bpm = MutableStateFlow(120)
    val bpm = _bpm.asStateFlow()

    private val _isTicking = MutableStateFlow(false)
    val isTicking = _isTicking.asStateFlow()

    var currentBeat by mutableStateOf(1)

    val bpmOptions = (40..220).toList()

    private val _beatsPerMeasure = MutableStateFlow(4)
    val beatsPerMeasure = _beatsPerMeasure.asStateFlow()

    val measureOptions = (1..16).toList()

    private var internalTickCount = 1

    init {
        engine.onTick = {
            val isAccent = (internalTickCount == 1)
            engine.playTick(isAccent)
            currentBeat = internalTickCount
            _tickEvent.tryEmit(internalTickCount)
            internalTickCount = if (internalTickCount >= beatsPerMeasure.value) 1 else internalTickCount + 1
        }
    }

    fun toggleMetronome() {
        if (_isTicking.value) {
            engine.stop()
        } else {
            internalTickCount = 1
            currentBeat = 1
            engine.setBpm(_bpm.value)
            engine.start()
        }
        _isTicking.value = !_isTicking.value
    }

    fun updateBpm(newBpm: Int) {
        val clamped = newBpm.coerceIn(40, 220)
        _bpm.value = clamped
        engine.setBpm(newBpm)
    }

    fun updateBeatsPerMeasure(newBeats: Int) {
        _beatsPerMeasure.value = newBeats
    }

    override fun onCleared() {
        engine.stop()
        super.onCleared()
    }

}