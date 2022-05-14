package com.daniil.shevtsov.idle.feature.initial.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import org.junit.jupiter.api.Test

internal class InitialFunctionalCoreTest {

    @Test
    fun `should use mutanity as main ratio of devourer`() {
        val state = gameState(
            ratios = listOf(ratio(key = RatioKey.Mutanity)),
            player = player(traits = mapOf(
                TraitId.Species to playerTrait(id = Species.Devourer.id)
            )),
        )

        assertThat(state).prop(GameState::mainRatioKey).isEqualTo(RatioKey.Mutanity)
    }

}
