package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlayerFunctionalCoreTest {

    @Test
    fun `should update player traits when trait selected`() {
        val initialPlayerTrait = playerTrait(id = 1L, title = "Initial Job")
        val newPlayerTrait = playerTrait(id = 2L, title = "new Job")

        val initialPlayer = player(
            traits = mapOf(
                TraitId.Job to initialPlayerTrait
            ),
            generalTags = listOf(tag("Non trait tag"))
        )
        val initialState = gameState(
            player = initialPlayer,
            availableTraits = listOf(
                initialPlayerTrait,
                newPlayerTrait,

            )
        )
        val newState = gameState(
            player = player(
                traits = mapOf(
                    TraitId.Job to newPlayerTrait
                )
            )
        )
//            playerFunctionalCore(
//            state = initialState,
//            action = PlayerViewAction.ChangeTrait(traitId = TraitId.Job, id = 2L),
//        )
    }

}
