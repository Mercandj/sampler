package com.mercandalli.android.apps.sampler.audio

import android.annotation.SuppressLint
import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

internal class SoundPoolAudioManager(
    private val assetManager: AssetManager
) : AudioManager {

    @Suppress("DEPRECATION")
    private val soundPool: SoundPool = SoundPool(
        20,
        android.media.AudioManager.STREAM_MUSIC,
        0
    )

    @SuppressLint("UseSparseArrays")
    private var streamIdToLoaded = HashMap<Int, Boolean>()
    private var streamIds = HashMap<String, Int>()

    override fun load(assetsFilePaths: List<String>) {
        for (assetsFilePath in assetsFilePaths) {
            streamIds[assetsFilePath] = loadSound(assetsFilePath)
        }
    }

    override fun play(assetsFilePath: String) {
        val streamId = getLoadedStreamId(assetsFilePath) ?: return
        soundPool.play(streamId, 1F, 1F, 1, 0, 1f)
    }

    override fun stop(assetsFilePath: String) {
        val streamId = getLoadedStreamId(assetsFilePath) ?: return
        soundPool.stop(streamId)
    }

    @Suppress("ObjectLiteralToLambda")
    private fun loadSound(strSound: String): Int {
        soundPool.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(soundPool: SoundPool?, streamId: Int, status: Int) {
                streamIdToLoaded[streamId] = true
            }
        })
        try {
            return soundPool.load(assetManager.openFd(strSound), 1)
        } catch (e: IOException) {
            Log.e("SoundPoolAudioManager", "load error", e)
        }
        return -1
    }

    // Null if not loaded
    private fun getLoadedStreamId(assetsFilePath: String): Int? {
        if (!isLoaded(assetsFilePath)) {
            return null
        }
        return streamIds[assetsFilePath]!!
    }

    private fun isLoaded(assetsFilePath: String): Boolean {
        val streamId = streamIds[assetsFilePath]
        if (!streamIdToLoaded.containsKey(streamId)) {
            return false
        }
        return streamIdToLoaded[streamIds[assetsFilePath]]!!
    }
}
