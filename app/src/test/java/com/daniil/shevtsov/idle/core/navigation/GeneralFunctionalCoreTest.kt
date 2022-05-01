package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class GeneralFunctionalCoreTest {
    @Test
    fun `should replace current screen when opening another`() {
        val state = generalFunctionalCore(
            state = gameState(currentScreen = Screen.Main),
            viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame)
        )

        assertThat(state)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.FinishedGame)
    }

    @Test
    fun `should add screen to stack when opening it`() {
        val state = generalFunctionalCore(
            state = gameState(currentScreen = Screen.Main, screenStack = listOf(Screen.Main)),
            viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame)
        )

        assertThat(state)
            .prop(GameState::screenStack)
            .containsExactly(Screen.Main, Screen.FinishedGame)
    }

    @Test
    fun `should go back when back clicked`() {
        val state = generalFunctionalCore(
            state = gameState(
                currentScreen = Screen.FinishedGame,
                screenStack = listOf(Screen.Main, Screen.FinishedGame)
            ),
            viewAction = GeneralViewAction.Back
        )

        assertThat(state)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.Main)
    }

    @Test
    fun `should remove last element from screen stack when back clicked`() {
        val state = generalFunctionalCore(
            state = gameState(
                currentScreen = Screen.FinishedGame,
                screenStack = listOf(Screen.Main, Screen.FinishedGame)
            ),
            viewAction = GeneralViewAction.Back
        )

        assertThat(state)
            .prop(GameState::screenStack)
            .containsExactly(Screen.Main)
    }

}
