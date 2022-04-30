package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.gamefinish.domain.ending
import com.daniil.shevtsov.idle.feature.gamefinish.domain.finishedGameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.unlock
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import org.junit.jupiter.api.Test

internal class FinishedGamePresentationTest {
    @Test
    fun `should show description of ending`() {
        val ending = ending(
            title = "EPIC FAIL",
            description = "YOU GET REKT M8"
        )
        val state = finishedGameState(
            endings = listOf(ending),
        )

        val viewState = mapFinishedGameViewState(state = state)

        assertThat(viewState)
            .extractingEndingDescription()
            .isEqualTo(ending.description)
    }

    @Test
    fun `should show unlocks of ending`() {
        val unlockTag = tag(name = "Folders of memes")
        val unlock = unlock(
            title = "Scholar of Memes",
            newTags = listOf(unlockTag)
        )
        val ending = ending(
            unlocks = listOf(unlock)
        )
        val state = finishedGameState(
            endings = listOf(ending),
        )

        val viewState = mapFinishedGameViewState(state = state)

        assertThat(viewState)
            .extractingEndingUnlocks()
            .index(0)
            .all {
                prop(UnlockModel::title).isEqualTo(unlock.title)
                prop(UnlockModel::subtitle).isEqualTo(unlock.description)
                prop(UnlockModel::unlockFeatures)
                    .index(0)
                    .prop(UnlockFeatureModel::title)
                    .isEqualTo(unlockTag.name)
            }
    }

    private fun Assert<FinishedGameViewState>.extractingEndingState() =
        prop(FinishedGameViewState::endingState)

    private fun Assert<FinishedGameViewState>.extractingEndingDescription() =
        extractingEndingState()
            .prop(EndingViewState::description)

    private fun Assert<FinishedGameViewState>.extractingEndingUnlocks() =
        prop(FinishedGameViewState::unlocks)
}
