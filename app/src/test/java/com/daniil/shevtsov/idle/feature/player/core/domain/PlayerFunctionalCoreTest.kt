package com.daniil.shevtsov.idle.feature.player.core.domain

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayerFunctionalCoreTest {

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
            player = initialPlayer,
            availableTraits = listOf(
                initialPlayerTrait,
                newPlayerTrait,
            )
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

}
