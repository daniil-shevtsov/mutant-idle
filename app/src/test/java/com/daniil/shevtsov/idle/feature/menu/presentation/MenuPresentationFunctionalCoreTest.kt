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

internal class MenuPresentationFunctionalCoreTest {

    @Test
    fun `should create initial view state`() {
        val state = mapMenuViewState(state = gameState())

        assertThat(state)
            .all {
                title().isEqualTo("Mutant Idle")
                prop(MenuViewState::buttons)
                    .extracting(MenuButtonModel::title)
                    .containsExactly(
                        "Start Game",
                        "Settings",
                    )
            }
    }

}
