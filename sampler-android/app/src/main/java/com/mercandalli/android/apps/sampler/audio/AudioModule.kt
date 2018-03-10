package com.mercandalli.android.apps.sampler.audio

class AudioModule {

    fun provideAudioManager(): AudioManager {
        return NativeAudioManager()
    }

}