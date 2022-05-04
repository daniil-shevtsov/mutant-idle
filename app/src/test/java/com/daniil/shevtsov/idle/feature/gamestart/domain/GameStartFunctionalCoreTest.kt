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
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import org.junit.jupiter.api.Test

internal class GameStartFunctionalCoreTest {

    @Test
    fun `should choose species when species clicked`() {
        val nonSpeciesTags = listOf(
            tag(name = "non-species tag 1"),
            tag(name = "non-species tag 2"),
        )

        val previousSpecies = playerSpecies(
            id = 0L,
            title = "old species",
            tags = listOf(
                tag(name = "old species tag 1"),
                tag(name = "old species tag 2"),
            )
        )
        val newSpecies = playerSpecies(
            id = 1L,
            title = "new species",
            tags = listOf(
                tag(name = "new species tag 1"),
                tag(name = "new species tag 2"),
            ),
        )

        val previousPlayerState = player(
            species = previousSpecies,
            generalTags = nonSpeciesTags,
        )

        val initialState = gameState(
            player = previousPlayerState,
            availableSpecies = listOf(
                previousPlayerState.species,
                newSpecies
            ),
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.SpeciesSelected(id = newSpecies.id)
        )

        assertThat(newState)
            .prop(GameState::player)
            .all {
                prop(Player::species)
                    .isEqualTo(newSpecies)

                prop(Player::tags)
                    .all {
                        containsSubList(nonSpeciesTags)
                        containsNone(previousPlayerState.species.tags)
                        containsSubList(newSpecies.tags)
                    }
            }
    }

    @Test
    fun `should open main screen when species clicked`() {
        val newSpecies = playerSpecies(
            id = 1L,
            title = "new species",
            tags = listOf(
                tag(name = "new species tag 1"),
                tag(name = "new species tag 2"),
            ),
        )

        val initialState = gameState(
            availableSpecies = listOf(
                newSpecies
            ),
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.SpeciesSelected(id = newSpecies.id)
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.Main)
    }

}
