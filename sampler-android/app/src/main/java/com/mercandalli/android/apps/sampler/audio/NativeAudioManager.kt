package com.mercandalli.android.apps.sampler.audio

internal class NativeAudioManager internal constructor() : AudioManager {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun play() {
        nativePlay()
    }

    private external fun nativePlay(): String
}
