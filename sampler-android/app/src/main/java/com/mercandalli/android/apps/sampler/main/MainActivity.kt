package com.mercandalli.android.apps.sampler.main

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.TextView
import com.mercandalli.android.apps.sampler.R
import com.mercandalli.android.apps.sampler.audio.AudioManager
import com.mercandalli.android.apps.sampler.pad.SquaresView
import com.mercandalli.android.apps.sampler.sample.SampleManager
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.DECK_A
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.DECK_B

class MainActivity : AppCompatActivity() {

    private lateinit var audioManager: AudioManager
    private lateinit var sampleManager: SampleManager
    private lateinit var squaresView: SquaresView
    private lateinit var buttonSelectionA: TextView
    private lateinit var buttonSelectionB: TextView

    private var deckAColor: Int = 0
    private var deckBColor: Int = 0
    private var textColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainGraph.init(this)
        audioManager = MainGraph.get().provideAudioManager()
        sampleManager = MainGraph.get().provideSampleManager()
        audioManager.load(sampleManager.getSamples())

        deckAColor = ContextCompat.getColor(this, R.color.primary_color_deck_A)
        deckBColor = ContextCompat.getColor(this, R.color.primary_color_deck_B)
        textColor = ContextCompat.getColor(this, R.color.color_text)

        squaresView = findViewById(R.id.activity_main_squares_view)
        squaresView.setOnSquareChangedListener(object : SquaresView.OnSquareChangedListener {
            override fun onSquareCheckedChanged(idButton: Int, isChecked: Boolean) {
                if (isChecked) {
                    audioManager.play(sampleManager.mapPositionToPath(idButton))
                }
            }
        })
        buttonSelectionA = findViewById(R.id.activity_main_deck_a)
        buttonSelectionA.setOnClickListener {
            sampleManager.setCurrentDeck(DECK_A)
            onDeckSelected(sampleManager.getCurrentDeck())
        }
        buttonSelectionB = findViewById(R.id.activity_main_deck_b)
        buttonSelectionB.setOnClickListener {
            sampleManager.setCurrentDeck(DECK_B)
            onDeckSelected(sampleManager.getCurrentDeck())
        }
        onDeckSelected(sampleManager.getCurrentDeck())
    }

    private fun onDeckSelected(@SampleManager.Companion.Deck currentDeck: Long) {
        when (currentDeck) {
            DECK_A -> {
                squaresView.setStyle(deckAColor)
                squaresView.setTexts(SquaresView.TEXT_BUTTONS_A)
                buttonSelectionA.setTextColor(deckAColor)
                buttonSelectionB.setTextColor(textColor)
            }
            DECK_B -> {
                squaresView.setStyle(deckBColor)
                squaresView.setTexts(SquaresView.TEXT_BUTTONS_B)
                buttonSelectionA.setTextColor(textColor)
                buttonSelectionB.setTextColor(deckBColor)
            }
        }
    }
}
