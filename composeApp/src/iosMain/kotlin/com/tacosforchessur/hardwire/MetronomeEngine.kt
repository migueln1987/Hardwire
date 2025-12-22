package com.tacosforchessur.hardwire

import com.tacosforchessur.hardwire.core.logging.Logger
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
        println("engine::starting metronome engine engine")
        if (!isRunning.compareAndSet(expectedValue = false, newValue = true)) return
        job = scope.launch {
                while (isRunning.load()) {
                    val interval = 60000L / bpm
                    Logger.d("engine::invoking tick")
                    onTick?.invoke()
                    Logger.d("engine::delaying $interval")
                    delay(interval)
                }
            }
    }

    actual fun stop() {
        isRunning.store(false)
        job?.cancel()
    }

    actual fun setBpm(bpm: Int) {
        Logger.d("engine::bpm being set to $bpm")
        this.bpm = bpm
    }

    actual fun isPlaying(): Boolean = isRunning.load()

    actual fun playTick(isAccent: Boolean) {
        Logger.d("engine::playing tick ${if (isAccent) " with accent" else "without accent" }")
        val note = if (isAccent) 84u.toUByte() else 72u.toUByte()
        val velocity = if (isAccent) 127u.toUByte() else 100u.toUByte()
        Logger.d("engine::starting note")
        sampler.startNote(note, velocity, 0u.toUByte())
        Logger.d("engine::starting coroutine")
        scope.launch {
            Logger.d("engine::delaying note")
            delay(60)
            Logger.d("engine::stopping note")
            sampler.stopNote(note, 0u.toUByte())
        }
    }

}
