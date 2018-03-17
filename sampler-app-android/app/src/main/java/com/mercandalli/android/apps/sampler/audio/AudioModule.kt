package com.mercandalli.android.apps.sampler.audio

import android.content.Context

class AudioModule constructor(private val context: Context) {

    @Suppress("ConstantConditionIf")
    fun provideAudioManager(): AudioManager {
        return if (NativeAudioManager) {
            NativeAudioManager(context)
        } else {
            SoundPoolAudioManager(context.assets)
        }
    }

    companion object {
        private const val NativeAudioManager = false
    }

}