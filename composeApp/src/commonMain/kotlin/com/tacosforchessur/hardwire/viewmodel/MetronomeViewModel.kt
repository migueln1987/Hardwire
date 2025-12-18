package com.tacosforchessur.hardwire.viewmodel

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

    private val _tickEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val ticketEvent = _tickEvent.asSharedFlow()

    private val _bpm = MutableStateFlow(120)
    val bpm = _bpm.asStateFlow()

    private val _isTicking = MutableStateFlow(false)
    val isTicking = _isTicking.asStateFlow()

    init {
        engine.onTick = {
            _tickEvent.tryEmit(Unit)
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

    fun startMetronome() {
        viewModelScope.launch {
            println("TRACE: 1. Start button clicked")
            try {
                println("TRACE: 2. Attempting to read bytes...")
                val audioBytes = Res.readBytes("files/tik.wav")
                println("TRACE: 3. Successfully read ${audioBytes.size} bytes")

                engine.setAudioData(audioBytes)
                println("TRACE: 4. Data sent to engine")

                engine.start()
                println("TRACE: 5. Engine.start() called")
            } catch (e: Exception) {
                println("TRACE: ERROR - ${e.message}")
                e.printStackTrace()
            }

        }
    }
}