package com.mercandalli.android.apps.sampler.audio

import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

internal class SoundPoolAudioManager constructor(
        private val assetManager: AssetManager) : AudioManager {

    private val soundPool: SoundPool = SoundPool(20, android.media.AudioManager.STREAM_MUSIC, 0)

    private var loaded = HashMap<Int, Boolean>()
    private var slots = HashMap<String, Int>()

    override fun load(assetsFilePaths: List<String>) {
        for (assetsFilePath in assetsFilePaths) {
            slots[assetsFilePath] = loadSound(assetsFilePath)
        }
    }

    override fun play(assetsFilePath: String) {
        if (loaded.containsKey(slots[assetsFilePath]) && loaded[slots[assetsFilePath]]!!) {
            soundPool.play(slots[assetsFilePath]!!, 1F, 1F, 1, 0, 1f)
        }
    }

    @Suppress("ObjectLiteralToLambda")
    private fun loadSound(strSound: String): Int {
        soundPool.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
                loaded[sampleId] = true
            }
        })
        try {
            return soundPool.load(assetManager.openFd(strSound), 1)
        } catch (e: IOException) {
            Log.e("SoundPoolAudioManager", "load error", e)
        }
        return -1
    }
}