package com.mercandalli.android.apps.sampler.main

import android.content.Context
import com.mercandalli.android.apps.sampler.audio.AudioManager
import com.mercandalli.android.apps.sampler.audio.AudioModule

class MainGraph private constructor(context: Context) {

    private val audioManager: AudioManager = AudioModule(context).provideAudioManager()

    fun provideAudioManager(): AudioManager {
        return audioManager
    }

    companion object {
        private var mainGraph: MainGraph? = null

        @JvmStatic
        fun init(context: Context): MainGraph {
            if (mainGraph == null) {
                mainGraph = MainGraph(context.applicationContext)
            }
            return mainGraph!!
        }

        @JvmStatic
        fun get(): MainGraph {
            return mainGraph!!
        }
    }

}