package com.mercandalli.android.apps.sampler.main

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mercandalli.android.apps.sampler.R
import com.mercandalli.android.apps.sampler.audio.AudioManager
import com.mercandalli.android.apps.sampler.pad.SquaresView

class MainActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var squaresView: SquaresView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainGraph.init(this)
        audioManager = MainGraph.get().provideAudioManager()
        audioManager.load(listOf(
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
                "wav/shape-of-you/dpm_shape_of_you_a_vox_01.wav"))
        squaresView = findViewById(R.id.activity_main_squares_view)
        squaresView.setOnSquareChangedListener(object : SquaresView.OnSquareChangedListener {
            override fun onSquareCheckedChanged(idButton: Int, isChecked: Boolean) {
                if (isChecked) {
                    audioManager.play(idToPath(idButton))
                }
            }
        })
    }

    private fun idToPath(id: Int): String {
        return when (id) {
            SquaresView.BeatGridPreset_A -> "wav/shape-of-you/dpm_shape_of_you_a_bass_01.wav"
            SquaresView.BeatGridPreset_B -> "wav/shape-of-you/dpm_shape_of_you_a_bass_02.wav"
            SquaresView.BeatGridPreset_C -> "wav/shape-of-you/dpm_shape_of_you_a_bass_03.wav"
            SquaresView.BeatGridPreset_D -> "wav/shape-of-you/dpm_shape_of_you_a_bass_04.wav"
            SquaresView.BeatGridPreset_E -> "wav/shape-of-you/dpm_shape_of_you_a_hat_01.wav"
            SquaresView.BeatGridPreset_F -> "wav/shape-of-you/dpm_shape_of_you_a_kick_01.wav"
            SquaresView.BeatGridPreset_G -> "wav/shape-of-you/dpm_shape_of_you_a_melody_01.wav"
            SquaresView.BeatGridPreset_H -> "wav/shape-of-you/dpm_shape_of_you_a_melody_02.wav"
            SquaresView.BeatGridPreset_I -> "wav/shape-of-you/dpm_shape_of_you_a_melody_03.wav"
            SquaresView.BeatGridPreset_J -> "wav/shape-of-you/dpm_shape_of_you_a_melody_04.wav"
            SquaresView.BeatGridPreset_K -> "wav/shape-of-you/dpm_shape_of_you_a_snare_01.wav"
            SquaresView.BeatGridPreset_L -> "wav/shape-of-you/dpm_shape_of_you_a_vox_01.wav"
            else -> throw Exception("Unknown id: $id")
        }
    }
}
