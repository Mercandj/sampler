package com.mercandalli.android.apps.sampler.main

import android.content.Context
import com.mercandalli.android.apps.sampler.audio.AudioManager
import com.mercandalli.android.apps.sampler.audio.AudioModule
import com.mercandalli.android.apps.sampler.sample.SampleManager
import com.mercandalli.android.apps.sampler.sample.SampleModule

class MainGraph private constructor(
    context: Context
) {

    private val audioManager: AudioManager = AudioModule(context).createAudioManager()
    private val sampleManager: SampleManager = SampleModule().createSampleManager()

    companion object {

        private var mainGraph: MainGraph? = null

        @JvmStatic
        fun init(context: Context): MainGraph {
            if (mainGraph == null) {
                mainGraph = MainGraph(context.applicationContext)
            }
            return mainGraph!!
        }

        fun getAudioManager(): AudioManager {
            return mainGraph!!.audioManager
        }

        fun getSampleManager(): SampleManager {
            return mainGraph!!.sampleManager
        }
    }
}
