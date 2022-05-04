package com.daniil.shevtsov.idle.feature.gamestart.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
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
        val species1 = playerSpecies(id = 1L, title = "species 1", description = "description 1")
        val species2 = playerSpecies(id = 2L, title = "species 2", description = "description 2")
        val state = gameState(
            availableSpecies = listOf(
                species1,
                species2,
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .all {
                index(0).all {
                    prop(SpeciesSelectionItem::title).isEqualTo(species1.title)
                    prop(SpeciesSelectionItem::description).isEqualTo(species1.description)
                    prop(SpeciesSelectionItem::icon).isEqualTo(species1.icon)
                }
                index(1).all {
                    prop(SpeciesSelectionItem::title).isEqualTo(species2.title)
                    prop(SpeciesSelectionItem::description).isEqualTo(species2.description)
                    prop(SpeciesSelectionItem::icon).isEqualTo(species2.icon)
                }
            }
    }

}
