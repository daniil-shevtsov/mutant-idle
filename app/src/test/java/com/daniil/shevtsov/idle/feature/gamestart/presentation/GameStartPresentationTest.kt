package com.daniil.shevtsov.idle.feature.gamestart.presentation

import assertk.assertThat
import assertk.assertions.isNotNull
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import org.junit.jupiter.api.Test

internal class GameStartPresentationTest {

    @Test
    fun `should show title initially`() {
        val state = gameState()

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .isNotNull()
    }

}
