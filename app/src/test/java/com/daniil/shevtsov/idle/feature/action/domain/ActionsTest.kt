package com.daniil.shevtsov.idle.feature.action.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import org.junit.jupiter.api.Test

internal class ActionsTest {

    fun actionClicked(oldState: GameState, id: Long): GameState {
        return oldState
    }

    @Test
    fun `should add little suspicion when invisible`() {
        val action = action(id = 1L)
        val oldState = gameState(
            actions = listOf(action),
            ratios = listOf(ratio(key = RatioKey.Suspicion, value = 10.0)),
            player = player(generalTags = listOf(Tags.State.Invisible)),
        )

        val newState = actionClicked(oldState, action.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(11.0)
    }

    private fun Assert<GameState>.extractingSuspicion() = prop(GameState::ratios)
        .index(0)
        .prop(Ratio::value)


}
