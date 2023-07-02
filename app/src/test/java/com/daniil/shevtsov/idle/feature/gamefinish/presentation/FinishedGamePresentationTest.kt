package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.ending
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
        val state = gameState(
            allEndings = listOf(ending),
            currentEndingId = ending.id,
        )

        val viewState = mapFinishedGameViewState(state = state)

        assertThat(viewState)
            .extractingEndingDescription()
            .isEqualTo(ending.description)
    }

    @Test
    fun `should show unlocks of ending`() {
        val unlockTag = tag(
            name = "Folders of memes",
            description = "Years of hoarding were not in vain"
        )
        val unlock = unlock(
            title = "Scholar of Memes",
            newTags = listOf(unlockTag)
        )
        val ending = ending(
            unlocks = listOf(unlock)
        )
        val state = gameState(
            allEndings = listOf(ending),
            currentEndingId = ending.id
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
                    .all {
                        prop(UnlockFeatureModel::title).isEqualTo(unlockTag.name)
                        prop(UnlockFeatureModel::subtitle).isEqualTo(unlockTag.description)
                    }
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
