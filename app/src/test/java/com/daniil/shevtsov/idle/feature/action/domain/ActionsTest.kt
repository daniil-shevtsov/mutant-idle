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
        val action = oldState.actions.find { it.id == id }!!
        val ratioKey = RatioKey.Suspicion
        val ratioChange = action.ratioChanges[ratioKey]!!
        val oldRatio = oldState.ratios.find { ratio -> ratio.key == ratioKey }!!
        val newRatio = oldRatio.copy(value = oldRatio.value + ratioChange)

        return oldState.copy(
            ratios = oldState.ratios.map { ratio ->
                when (ratio.key) {
                    newRatio.key -> newRatio
                    else -> ratio
                }
            }
        )
    }

    private val action = action(id = 1L, ratioChanges = mapOf(RatioKey.Suspicion to 5.0))
    private val initialState = gameState(
        actions = listOf(action),
        ratios = listOf(ratio(key = RatioKey.Suspicion, value = 10.0)),
    )

    @Test
    fun `should add much suspicion when visible`() {
        val oldState = initialState
        val newState = actionClicked(oldState, action.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(15.0)
    }

    @Test
    fun `should add little suspicion when invisible`() {
        val oldState = initialState.copy(
            player = player(generalTags = listOf(Tags.State.Invisible))
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
