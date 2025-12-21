package com.tacosforchessur.hardwire

expect class MetronomeEngine() {

    var onTick: (() -> Unit)?

    fun start()
    fun stop()
    fun setBpm(bpm: Int)

    fun isPlaying(): Boolean

    fun playTick(isAccent: Boolean)
}