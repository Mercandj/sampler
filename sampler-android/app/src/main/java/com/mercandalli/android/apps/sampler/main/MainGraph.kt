package com.mercandalli.android.apps.sampler.main

import com.mercandalli.android.apps.sampler.audio.AudioManager
import com.mercandalli.android.apps.sampler.audio.AudioModule

class MainGraph private constructor() {

    private val audioManager: AudioManager = AudioModule().provideAudioManager()

    fun provideAudioManager():AudioManager {
        return audioManager
    }

    companion object {
        private var mainGraph: MainGraph? = null

        @JvmStatic
        fun get(): MainGraph {
            if (mainGraph == null) {
                mainGraph = MainGraph()
            }
            return mainGraph!!
        }
    }

}