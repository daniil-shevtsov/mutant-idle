package com.daniil.shevtsov.idle.feature.gamefinish.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import org.junit.jupiter.api.Test

class FinishedGameFunctionalCoreTest {
    @Test
    fun `should open game start screen when start new game clicked`() {
        val state = mainFunctionalCore(
            state = gameState(currentScreen = Screen.FinishedGame, screenStack = listOf(Screen.FinishedGame)),
            viewAction = MainViewAction.StartNewGameClicked
        )

        assertThat(state)
            .all {
                prop(GameState::currentScreen)
                    .isEqualTo(Screen.GameStart)
                prop(GameState::screenStack).isEmpty()
            }
    }
}
