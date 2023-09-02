package com.daniil.shevtsov.idle.feature.player.core.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsNone
import assertk.assertions.containsOnly
import assertk.assertions.containsSubList
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import org.junit.jupiter.api.Test

internal class PlayerFunctionalCoreTest {

    @Test
    fun `should add selected trait tags into player tags`() {
        val jobTag = tag(name = "job tag")
        val speciesTag = tag(name = "species tag")
        val player = player(
            traits = mapOf(
                TraitId.Job to playerTrait(tags = listOf(jobTag)),
                TraitId.Species to playerTrait(tags = listOf(speciesTag)),
            )
        )

        assertThat(player::tags)
            .containsAll(jobTag, speciesTag)
    }

    @Test
    fun `should update player traits when trait selected`() {
        val initialPlayerTrait = playerTrait(id = 1L)
        val newPlayerTrait = playerTrait(id = 2L)

        val initialPlayer = player(
            traits = mapOf(
                initialPlayerTrait.traitId to initialPlayerTrait
            )
        )
        val initialState = gameState(
            availableTraits = listOf(
                initialPlayerTrait,
                newPlayerTrait,
            ),
            player = initialPlayer,
            gameTitle = MenuTitleState.Result("Mutant Idle")
        )

        val newState = playerFunctionalCore(
            state = initialState,
            action = PlayerViewAction.ChangeTrait(
                traitId = newPlayerTrait.traitId,
                id = newPlayerTrait.id,
            ),
        )

        assertThat(newState)
            .prop(GameState::player)
            .prop(Player::traits)
            .containsOnly(
                newPlayerTrait.traitId to newPlayerTrait
            )
    }

    @Test
    fun `should replace previous trait tags with the new ones`() {
        val initialPlayerTrait =
            playerTrait(id = 1L, tags = listOf(tag(name = "initial trait tag")))
        val newPlayerTrait = playerTrait(id = 2L, tags = listOf(tag(name = "new trait tag")))

        val initialPlayer = player(
            generalTags = listOf(tag(name = "non-trait tag")),
            traits = mapOf(
                initialPlayerTrait.traitId to initialPlayerTrait
            )
        )
        val initialState = gameState(
            availableTraits = listOf(
                initialPlayerTrait,
                newPlayerTrait,
            ),
            player = initialPlayer,
            gameTitle = MenuTitleState.Result("Mutant Idle")
        )

        val newState = playerFunctionalCore(
            state = initialState,
            action = PlayerViewAction.ChangeTrait(
                traitId = newPlayerTrait.traitId,
                id = newPlayerTrait.id,
            ),
        )

        assertThat(newState)
            .prop(GameState::player)
            .prop(Player::tags)
            .all {
                containsSubList(initialPlayer.generalTags)
                containsSubList(newPlayerTrait.tags)
                containsNone(initialPlayerTrait.tags)
            }
    }

}
