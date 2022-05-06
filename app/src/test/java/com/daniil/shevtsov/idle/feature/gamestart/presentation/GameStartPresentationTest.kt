package com.daniil.shevtsov.idle.feature.gamestart.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
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

    @Test
    fun `should show only unlocked species`() {
        val unlockedSpecies = playerSpecies(id = 1L, title = "unlocked species")
        val lockedSpecies = playerSpecies(id = 2L, title = "locked species")

        val state = gameState(
            availableSpecies = listOf(
                unlockedSpecies,
                lockedSpecies,
            ),
            unlockState = unlockState(
                species = mapOf(
                    unlockedSpecies.id to true,
                    lockedSpecies.id to false,
                )
            )
        )
        val viewState = mapGameStartViewState(state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .extracting(SpeciesSelectionItem::title)
            .all {
                contains(unlockedSpecies.title)
                containsNone(lockedSpecies.title)
            }
    }

    @Test
    fun `should show selected species as selected`() {
        val notSelectedSpecies = playerSpecies(id = 1L, title = "not selected species")
        val selectedSpecies = playerSpecies(id = 2L, title = "selected species")
        val state = gameState(
            availableSpecies = listOf(
                notSelectedSpecies,
                selectedSpecies,
            ),
            player = player(
                species = selectedSpecies,
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .extracting(SpeciesSelectionItem::title, SpeciesSelectionItem::isSelected)
            .containsExactly(
                notSelectedSpecies.title to false,
                selectedSpecies.title to true,
            )
    }

}
