package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.isInstanceOf
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class ScreenPresentationTest {
    @Test
    fun `should form main view state when main screen selected`() {
        val state = gameState(currentScreen = Screen.Main)

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState).isInstanceOf(ScreenViewState.Main::class)
    }

    @Test
    fun `should form finished game view state when finished game screen selected`() {
        val state = gameState(currentScreen = Screen.FinishedGame)

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState).isInstanceOf(ScreenViewState.FinishedGame::class)
    }
}