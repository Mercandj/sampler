package com.mercandalli.android.apps.sampler.audio

import android.content.res.AssetManager
import android.media.SoundPool
import android.util.Log
import java.io.IOException

internal class SoundPoolAudioManager constructor(
        private val assetManager: AssetManager) : AudioManager {

    private val soundPool: SoundPool = SoundPool(20, android.media.AudioManager.STREAM_MUSIC, 0)

    private var sound1Loaded: Boolean = false
    private var sound1Slot: Int = 0

    override fun load() {
        sound1Slot = loadSound("wav/batterie-baguettes-1.wav")
    }

    override fun play() {
        if (sound1Loaded) {
            soundPool.play(sound1Slot, 1F, 1F, 1, 0, 1f)
        }
    }

    @Suppress("ObjectLiteralToLambda")
    private fun loadSound(strSound: String): Int {
        soundPool.setOnLoadCompleteListener(object : SoundPool.OnLoadCompleteListener {
            override fun onLoadComplete(soundPool: SoundPool?, sampleId: Int, status: Int) {
                sound1Loaded = true
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