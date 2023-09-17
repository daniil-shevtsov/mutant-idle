package com.daniil.shevtsov.idle.feature.menu.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.title
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class MainMenuPresentationFunctionalCoreTest {

    @Test
    fun `should create initial view state`() {
        val state = mapMainMenuViewState(
            state = gameState(gameTitle = MenuTitleState.Result("Mutant Idle"))
        )

        assertThat(state)
            .all {
                title().isEqualTo("Mutant Idle")
                prop(MainMenuViewState::buttons)
                    .extracting(MenuButtonModel::title)
                    .containsExactly(
                        "Start Game",
                        "Settings",
                    )
            }
    }

    @Test
    fun `should use another title`() {
        val state = mapMainMenuViewState(
            state = gameState(gameTitle = MenuTitleState.Result("Misha Idle"))
        )

        assertThat(state)
            .all {
                title().isEqualTo("Misha Idle")
                prop(MainMenuViewState::buttons)
                    .extracting(MenuButtonModel::title)
                    .containsExactly(
                        "Start Game",
                        "Settings",
                    )
            }
    }

}
