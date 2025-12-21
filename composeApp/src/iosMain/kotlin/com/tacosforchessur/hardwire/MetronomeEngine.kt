package com.tacosforchessur.hardwire

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioEngine
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.AVAudioUnitSampler
import platform.AVFAudio.setActive
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)
actual class MetronomeEngine actual constructor() {

    actual var onTick: (() -> Unit)? = null

    //    private var audioPlayer: AVAudioPlayer? = null
    private val isRunning = AtomicBoolean(false)
    private var bpm: Int = 120
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    private val engine = AVAudioEngine()
    private val sampler = AVAudioUnitSampler()

    init {
        setupAudioSession()
    }

    private fun setupAudioSession() {
        val session = AVAudioSession.sharedInstance()
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setActive(true, error = null)

        engine.attachNode(sampler)
        engine.connect(sampler, engine.mainMixerNode, null)

        engine.prepare()
        engine.startAndReturnError(null)
    }

    actual fun start() {
        if (!isRunning.compareAndSet(expectedValue = false, newValue = true)) return
        if (!engine.running) {
            engine.startAndReturnError(null)
        }
        job = scope.launch {
            delay(100)
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
    }

    actual fun isPlaying(): Boolean = isRunning.load()

    actual fun playTick(isAccent: Boolean) {
        val note = if (isAccent) 84u.toUByte() else 72u.toUByte()
        val velocity = if (isAccent) 127u.toUByte() else 100u.toUByte()

        sampler.startNote(note, velocity, 0u.toUByte())

        scope.launch {
            delay(60)
            sampler.stopNote(note, 0u.toUByte())
        }
    }

}
