package com.daniil.shevtsov.idle.feature.initial.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import org.junit.jupiter.api.Test

internal class InitialFunctionalCoreTest {

    @Test
    fun `initial screen should be the menu`() {
        val initialState = createInitialGameState()

        assertThat(initialState)
            .all {
                prop(GameState::screenStack).containsExactly(Screen.Menu)
                prop(GameState::currentScreen).isEqualTo(Screen.Menu)
            }
    }

}
