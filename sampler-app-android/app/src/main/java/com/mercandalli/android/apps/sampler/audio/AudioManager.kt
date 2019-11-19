package com.mercandalli.android.apps.sampler.audio

interface AudioManager {

    fun load(assetsFilePaths: List<String>)

    fun play(assetsFilePath: String)
}
