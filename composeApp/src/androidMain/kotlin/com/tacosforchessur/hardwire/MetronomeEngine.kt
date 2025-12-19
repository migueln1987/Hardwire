@file:OptIn(ExperimentalAtomicApi::class, ExperimentalAtomicApi::class)

package com.tacosforchessur.hardwire

import android.media.AudioAttributes
import android.media.SoundPool
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

actual class MetronomeEngine actual constructor() {
    actual var onTick: (() -> Unit)? = null

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    private val isRunning = AtomicBoolean(false)

    private var bpm: Int = 120

    private var isSoundLoaded = false

    private val toneGen = android.media.ToneGenerator(android.media.AudioManager.STREAM_MUSIC, 100)

    private val soundPool = SoundPool.Builder()
        .setMaxStreams(1)
        .setAudioAttributes(AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build())
        .build()

    private var soundId = 0

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    actual fun start() {
        if (!isRunning.compareAndSet(expectedValue = false, newValue = true)) return
        job = scope.launch {
            while (isRunning.load()) {
                val interval = 60000L / bpm
                onTick?.invoke()

                delay(interval)
            }
        }
    }

    actual fun stop() {
        isRunning.store(false)
        job?.cancel()
    }

    actual fun setBpm(bpm: Int) {
        this.bpm = bpm
    }

    actual fun setAudioData(data: ByteArray) {
        val tempFile = java.io.File.createTempFile("tik", "wav")
        tempFile.writeBytes(data)
        soundPool.setOnLoadCompleteListener { _, _, status ->
            if (status == 0) isSoundLoaded = true
        }
        soundId = soundPool.load(tempFile.absolutePath, 1)
    }

    actual fun isPlaying(): Boolean = isRunning.load()

    actual fun playTick(isAccent: Boolean) {
        if (isAccent) {
            toneGen.startTone(android.media.ToneGenerator.TONE_DTMF_S, 60)
        } else {
            toneGen.startTone(android.media.ToneGenerator.TONE_PROP_BEEP, 50)
        }
    }
}