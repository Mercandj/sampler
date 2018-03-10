package com.mercandalli.android.apps.sampler.audio

internal class NativeAudioManager internal constructor() : AudioManager {

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun load(assetPaths: List<String>) {

    }

    override fun play(assetPath: String) {
        nativePlay()
    }

    private external fun nativePlay(): String
}
