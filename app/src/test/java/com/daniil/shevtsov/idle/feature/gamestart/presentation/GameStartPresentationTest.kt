package com.daniil.shevtsov.idle.feature.gamestart.presentation

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import org.junit.jupiter.api.Test

internal class GameStartPresentationTest {

    @Test
    fun `should show title initially`() {
        val state = gameState()

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .isNotNull()
    }

    @Test
    fun `should show available species selection initially`() {
        val species1 = playerSpecies(id = 1L, title = "species 1")
        val species2 = playerSpecies(id = 2L, title = "species 2")
        val state = gameState(
            availableSpecies = listOf(
                species1,
                species2,
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .extracting(SpeciesSelectionItem::id, SpeciesSelectionItem::title)
            .containsExactly(
                species1.id to species1.title,
                species2.id to species2.title,
            )
    }

}
