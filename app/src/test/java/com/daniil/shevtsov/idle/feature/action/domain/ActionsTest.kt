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
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.satisfies
import org.junit.jupiter.api.Test

internal class ActionsTest {

    fun actionClicked(oldState: GameState, id: Long): GameState {
        val action = oldState.actions.find { it.id == id }!!
        val ratioKey = RatioKey.Suspicion
        val oldRatio = oldState.ratios.find { ratio -> ratio.key == ratioKey }!!

        val generalTags = oldState.player.generalTags

        val stealActionRatioChanges = mapOf(
            RatioKey.Suspicion to mapOf(
                listOf(Tags.State.Invisible) to 1.0,
                emptyList<Tag>() to 5.0,
            )
        )
        val humanActionRatioChanges = mapOf(
            RatioKey.Suspicion to mapOf(
                listOf(Tags.Appearance.Monster) to 10.0,
                emptyList<Tag>() to 0.0,
            )
        )

        val stealActionRatioChange = mapOf(
            RatioKey.Suspicion to when {
                generalTags.satisfies(TagRelation.RequiredAll, Tags.State.Invisible) -> 1.0
                else -> 5.0
            }
        )
        val humanRatioChange = mapOf(
            RatioKey.Suspicion to when {
                generalTags.satisfies(TagRelation.RequiredAll, Tags.Appearance.Human) -> 0.0
                generalTags.satisfies(TagRelation.RequiredAll, Tags.Appearance.Monster) -> 10.0
                else -> 0.0
            }
        )

        val ratioChanges = when (action) {
            stealAction -> stealActionRatioChanges
            humanAction -> humanActionRatioChanges
            else -> throw IllegalArgumentException()
        }

        val ratioChange = ratioChanges[RatioKey.Suspicion]?.minByOrNull { (tags, value) ->
            (tags - generalTags).size
        }?.value ?: 0.0

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
