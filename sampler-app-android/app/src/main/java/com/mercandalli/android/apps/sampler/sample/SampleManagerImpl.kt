package com.mercandalli.android.apps.sampler.sample

import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.DECK_A
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.deckASamples
import com.mercandalli.android.apps.sampler.sample.SampleManager.Companion.deckBSamples

internal class SampleManagerImpl : SampleManager {

    @SampleManager.Companion.Deck
    private var currentDeck = DECK_A

    override fun getCurrentDeck(): Int {
        return currentDeck
    }

    override fun setCurrentDeck(deck: Int) {
        currentDeck = deck
    }

    override fun getSamples(): List<String> {
        val list = ArrayList<String>()
        list.addAll(deckASamples)
        list.addAll(deckBSamples)
        return list
    }

    override fun getCurrentSamples(): List<String> {
        return if (currentDeck == DECK_A) deckASamples else deckBSamples
    }

    override fun mapPositionToPath(positionClicked: Int): String {
        return getCurrentSamples()[positionClicked]
    }
}
