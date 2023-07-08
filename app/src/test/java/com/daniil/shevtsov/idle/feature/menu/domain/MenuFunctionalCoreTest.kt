package com.daniil.shevtsov.idle.feature.menu.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuId
import org.junit.jupiter.api.Test

class MenuFunctionalCoreTest {

    @Test
    fun `should start game when start game button clicked`() {
        val state = mainFunctionalCore(
            state = createInitialGameState(),
            viewAction = MainViewAction.MenuButtonClicked(MenuId.StartGame)
        )

        assertThat(state)
            .all {
                prop(GameState::screenStack).containsExactly(
                    Screen.Menu,
                    Screen.GameStart
                )
                prop(GameState::currentScreen).isEqualTo(Screen.GameStart)
            }
    }

    @Test
    fun `should open settings when settings button clicked`() {
        val state = mainFunctionalCore(
            state = createInitialGameState(),
            viewAction = MainViewAction.MenuButtonClicked(MenuId.Settings)
        )

        assertThat(state)
            .all {
                prop(GameState::screenStack).containsExactly(
                    Screen.Menu,
                    Screen.Settings
                )
                prop(GameState::currentScreen).isEqualTo(Screen.Settings)
            }
    }

}
