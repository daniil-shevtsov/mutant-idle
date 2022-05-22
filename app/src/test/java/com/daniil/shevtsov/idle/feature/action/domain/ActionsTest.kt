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
        val oldRatio = oldState.ratios.find { ratio -> ratio.key == ratioKey }!!

        val isInvisible = oldState.player.generalTags.contains(Tags.State.Invisible)
        val isHuman = oldState.player.generalTags.contains(Tags.Appearance.Human)
        val isMonster = oldState.player.generalTags.contains(Tags.Appearance.Monster)

        val ratioChange = when {
            action == stealAction && isInvisible -> 1.0
            action == stealAction && !isInvisible -> 5.0
            action == humanAction && isHuman -> 0.0
            action == humanAction && isMonster -> 10.0
            else -> 0.0
        }
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

    private val stealAction = action(id = 1L, ratioChanges = mapOf(RatioKey.Suspicion to 5.0))
    private val humanAction = action(id = 2L, ratioChanges = mapOf(RatioKey.Suspicion to 0.0))
    private val initialState = gameState(
        actions = listOf(stealAction, humanAction),
        ratios = listOf(ratio(key = RatioKey.Suspicion, value = 10.0)),
    )

    @Test
    fun `should add much suspicion for stealing when visible`() {
        val oldState = initialState
        val newState = actionClicked(oldState, stealAction.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(15.0)
    }

    @Test
    fun `should add little suspicion for stealing when invisible`() {
        val oldState = initialState.copy(
            player = player(generalTags = listOf(Tags.State.Invisible))
        )

        val newState = actionClicked(oldState, stealAction.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(11.0)
    }

    @Test
    fun `should add no suspicion for human action when human appearance`() {
        val oldState = initialState.copy(
            player = player(generalTags = listOf(Tags.HumanAppearance))
        )

        val newState = actionClicked(oldState, humanAction.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(10.0)
    }

    @Test
    fun `should add suspicion for human action when not human appearance`() {
        val oldState = initialState.copy(
            player = player(generalTags = listOf(Tags.Appearance.Monster))
        )

        val newState = actionClicked(oldState, humanAction.id)

        assertThat(newState)
            .extractingSuspicion()
            .isEqualTo(20.0)
    }

    private fun Assert<GameState>.extractingSuspicion() = prop(GameState::ratios)
        .index(0)
        .prop(Ratio::value)


}
