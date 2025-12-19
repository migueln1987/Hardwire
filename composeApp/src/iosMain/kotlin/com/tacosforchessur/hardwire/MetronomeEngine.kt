package com.tacosforchessur.hardwire

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import platform.AVFAudio.AVAudioPlayer
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionCategoryPlayback
import platform.AVFAudio.setActive
import platform.Foundation.NSData
import platform.Foundation.create
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi

@OptIn(ExperimentalAtomicApi::class, ExperimentalForeignApi::class, BetaInteropApi::class)
actual class MetronomeEngine actual constructor() {

    actual var onTick: (() -> Unit)? = null
    private var audioPlayer: AVAudioPlayer? = null
    private val isRunning = AtomicBoolean(false)
    private var bpm: Int = 120
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var job: Job? = null

    init {
        val session = AVAudioSession.sharedInstance()
        session.setCategory(AVAudioSessionCategoryPlayback, error = null)
        session.setActive(true, error = null)
    }

    actual fun start() {
        if (isRunning.compareAndSet(false, true)) {
            job = scope.launch {
                while (isRunning.load()) {
                    val interval = 60000L / bpm
                    onTick?.invoke()

                    audioPlayer?.let {
                        if (it.playing) it.stop()
                        it.currentTime = 0.0
                        it.play()
                    }
                    delay(interval)
                }
            }
        }
    }

    actual fun stop() {
        isRunning.store(false)
        job?.cancel()
        audioPlayer?.stop()
    }

    actual fun setBpm(bpm: Int) {
        this.bpm = bpm
    }

    actual fun setAudioData(data: ByteArray) {
        val nsData = data.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = data.size.toULong()
            )
        }
        audioPlayer = AVAudioPlayer(data= nsData, error = null).apply {
            prepareToPlay()
        }
    }

    actual fun isPlaying(): Boolean = isRunning.load()

    actual fun playTick(isAccent: Boolean) {

    }

}
