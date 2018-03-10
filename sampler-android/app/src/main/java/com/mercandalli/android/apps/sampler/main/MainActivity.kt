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
        audioManager = MainGraph.get().provideAudioManager()
        squaresView = findViewById(R.id.activity_main_squares_view)
        squaresView.setOnSquareChangedListener(object : SquaresView.OnSquareChangedListener {
            override fun onSquareCheckedChanged(idButton: Int, isChecked: Boolean) {
                if (isChecked) {
                    audioManager.play()
                }
            }
        })
    }
}
