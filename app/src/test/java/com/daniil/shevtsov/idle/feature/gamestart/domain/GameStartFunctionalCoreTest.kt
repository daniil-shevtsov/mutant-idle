package com.daniil.shevtsov.idle.feature.gamestart.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsNone
import assertk.assertions.containsSubList
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.assertJobSelected
import com.daniil.shevtsov.idle.feature.player.core.domain.assertSpeciesSelected
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import org.junit.jupiter.api.Test

internal class GameStartFunctionalCoreTest {

    @Test
    fun `should not do anything when locked trait clicked`() {
        val newTrait = playerTrait(
            id = 2L,
            traitId = TraitId.Species,
            title = "new species",
            tags = listOf(tag(name = "kek"))
        )
        val initialPlayer = player()

        val initialState = gameState(
            player = initialPlayer,
            availableTraits = listOf(
                newTrait
            ),
            unlockState = unlockState(
                traits = mapOf(newTrait.traitId to mapOf(newTrait.id to false))
            )
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.TraitSelected(
                traitId = TraitId.Species,
                id = newTrait.id
            )
        )

        assertThat(newState)
            .prop(GameState::player)
            .isEqualTo(initialPlayer)
    }

    @Test
    fun `should choose trait when unlocked trait clicked`() {
        val nonSpeciesTags = listOf(
            tag(name = "non-species tag 1"),
            tag(name = "non-species tag 2"),
        )

        val previousSpecies = playerTrait(
            id = 0L,
            title = "old species",
            traitId = TraitId.Species,
            tags = listOf(
                tag(name = "old species tag 1"),
                tag(name = "old species tag 2"),
            )
        )
        val newSpecies = playerTrait(
            id = 1L,
            title = "new species",
            traitId = TraitId.Species,
            tags = listOf(
                tag(name = "new species tag 1"),
                tag(name = "new species tag 2"),
            ),
        )

        val previousPlayerState = player(
            generalTags = nonSpeciesTags,
            traits = mapOf(
                TraitId.Species to previousSpecies
            )
        )

        val initialState = gameState(
            player = previousPlayerState,
            availableTraits = listOf(
                previousSpecies,
                newSpecies,
            ),
            unlockState = unlockState(
                traits = mapOf(newSpecies.traitId to mapOf(newSpecies.id to true))
            ),
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.TraitSelected(
                traitId = TraitId.Species,
                id = newSpecies.id
            )
        )

        assertThat(newState)
            .prop(GameState::player)
            .assertSpeciesSelected(id = newSpecies.id)
    }

    @Test
    fun `should open main screen when start game clicked`() {
        val newState = gameStartFunctionalCore(
            state = gameState(),
            viewAction = GameStartViewAction.StartGame,
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.Main)
    }

}
