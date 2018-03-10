package com.mercandalli.android.apps.sampler.audio

interface AudioManager {
    fun load(assetPaths: List<String>)

    fun play(assetPath: String)
}
