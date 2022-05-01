package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
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
}
