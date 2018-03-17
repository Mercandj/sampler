package com.mercandalli.android.apps.sampler.sample

import android.support.annotation.IntDef

interface SampleManager {

    @Deck
    fun getCurrentDeck(): Int

    fun setCurrentDeck(@Deck deck: Int)

    fun getSamples(): List<String>

    fun getCurrentSamples(): List<String>

    fun mapPositionToPath(positionClicked: Int): String

    companion object {

        @IntDef(DECK_A, DECK_B)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Deck

        const val DECK_A = 0
        const val DECK_B = 1

        val deckASamples = listOf(
                "wav/shape-of-you/dpm_shape_of_you_a_bass_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_bass_02.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_bass_03.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_bass_04.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_hat_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_kick_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_melody_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_melody_02.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_melody_03.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_melody_04.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_snare_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_a_vox_01.wav")

        val deckBSamples = listOf(
                "wav/shape-of-you/dpm_shape_of_you_b_bass_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_bass_02.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_bass_03.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_bass_04.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_hat_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_kick_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_melody_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_melody_02.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_melody_03.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_melody_04.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_snare_01.wav",
                "wav/shape-of-you/dpm_shape_of_you_b_vox_01.wav")
    }

}