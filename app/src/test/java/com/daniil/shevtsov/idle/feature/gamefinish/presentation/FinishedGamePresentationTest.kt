package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import assertk.Assert
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.gamefinish.domain.ending
import com.daniil.shevtsov.idle.feature.gamefinish.domain.finishedGameState
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

    private fun Assert<FinishedGameViewState>.extractingEndingState() =
        prop(FinishedGameViewState::endingState)

    private fun Assert<FinishedGameViewState>.extractingEndingDescription() = extractingEndingState()
        .prop(EndingViewState::description)
}
