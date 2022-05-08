package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayerFunctionalCoreTest {

    @Test
    fun `should update player traits when trait selected`() {
        val initialPlayer = player(
            traits = mapOf(
                TraitId.Job to playerTrait(id = 1L, title = "Initial Job")
            )
        )
        val initialState = gameState(
            player = initialPlayer
        )
        val newState = playerFunctionalCore(
            state = initialState,
            action = PlayerViewAction.ChangeTrait(traitId = TraitId.Job, id = 2L),
        )
    }

}
