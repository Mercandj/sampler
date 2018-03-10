package com.mercandalli.android.apps.sampler.audio

import android.content.Context

class AudioModule constructor(private val context: Context) {

    fun provideAudioManager(): AudioManager {
        // return NativeAudioManager()
        return SoundPoolAudioManager(context.assets)
    }

}