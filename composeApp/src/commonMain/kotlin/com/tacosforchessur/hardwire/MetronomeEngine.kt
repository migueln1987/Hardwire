package com.tacosforchessur.hardwire

expect class MetronomeEngine() {

    var onTick: (() -> Unit)?

    fun setAudioData(data: ByteArray)
    fun start()
    fun stop()
    fun setBpm(bpm: Int)
    fun isPlaying(): Boolean
}