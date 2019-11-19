package com.mercandalli.android.apps.sampler.main

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mercandalli.android.apps.sampler.R
import com.mercandalli.android.apps.sampler.pad.SquaresView
import com.mercandalli.android.apps.sampler.sample.SampleManager
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.DECK_A
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.DECK_B

class MainActivity : AppCompatActivity() {

    private val audioManager by lazy { MainGraph.getAudioManager() }
    private val sampleManager by lazy { MainGraph.getSampleManager() }
    private lateinit var squaresView: SquaresView
    private lateinit var buttonSelectionA: TextView
    private lateinit var buttonSelectionB: TextView

    private var deckAColor: Int = 0
    private var deckBColor: Int = 0
    private var textColor: Int = 0

    private var progressPercent = Array(SquaresView.NB_COLUMN) { FloatArray(SquaresView.NB_LINE) }
    private var selectedX = -1
    private var selectedY = -1
    private val progressRunnable = createProgressRunnable()

    private fun postProgress() {
        squaresView.postDelayed(progressRunnable, 40)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MainGraph.init(this)
        audioManager.load(sampleManager.getSamples())

        deckAColor = ContextCompat.getColor(this, R.color.primary_color_deck_A)
        deckBColor = ContextCompat.getColor(this, R.color.primary_color_deck_B)
        textColor = ContextCompat.getColor(this, R.color.color_text)

        squaresView = findViewById(R.id.activity_main_squares_view)
        squaresView.setOnSquareChangedListener(object : SquaresView.OnSquareChangedListener {
            override fun onSquareCheckedChanged(idButton: Int, x: Int, y: Int, checked: Boolean) {
                if (checked) {
                    progressPercent = Array(SquaresView.NB_COLUMN) { FloatArray(SquaresView.NB_LINE) }
                    selectedX = x
                    selectedY = y
                    audioManager.play(sampleManager.mapPositionToPath(idButton))
                    squaresView.removeCallbacks(progressRunnable)
                    squaresView.post(progressRunnable)
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

    private fun onDeckSelected(@SampleManager.Companion.Deck currentDeck: Int) {
        when (currentDeck) {
            DECK_A -> {
                squaresView.setStyle(deckAColor)
                squaresView.setTexts(object : SquaresView.TextAddOn {
                    override fun getText(
                        column: Int,
                        columnSize: Int,
                        line: Int,
                        lineSize: Int
                    ): String {
                        val index = line + column * lineSize
                        return "A-$index"
                    }
                })
                buttonSelectionA.setTextColor(deckAColor)
                buttonSelectionB.setTextColor(textColor)
            }
            DECK_B -> {
                squaresView.setStyle(deckBColor)
                squaresView.setTexts(object : SquaresView.TextAddOn {
                    override fun getText(
                        column: Int,
                        columnSize: Int,
                        line: Int,
                        lineSize: Int
                    ): String {
                        val index = line + column * lineSize
                        return "B-$index"
                    }
                })
                buttonSelectionA.setTextColor(textColor)
                buttonSelectionB.setTextColor(deckBColor)
            }
        }
    }

    private fun createProgressRunnable(): Runnable {
        return Runnable {
            if (selectedX < 0 || selectedY < 0) {
                return@Runnable
            }
            progressPercent[selectedX][selectedY] += 0.06f
            if (progressPercent[selectedX][selectedY] > 1f) {
                progressPercent[selectedX][selectedY] = 0f
                selectedX = -1
                selectedY = -1
            }
            squaresView.setProgressBeatGrid(progressPercent)
            postProgress()
        }
    }
}
