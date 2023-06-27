package com.daniil.shevtsov.idle.feature.main.domain

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.location.domain.Location
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class PlotTest() {

    private val plotHolders = listOf(
        action(
            id = 1L,
            title = "test action",
            plot = "action plot"
        ) to "You performed action \"test action\"",
        location(
            id = 2L,
            title = "test location",
            plot = "location plot"
        ) to "You went to the test location",
    )

    @TestFactory
    fun testSquareRoots() = plotHolders
        .flatMap { (plotHolder: PlotHolder, expectedDefault: String) ->
            listOf(
                plotHolder to plotHolder.plot,
                plotHolder.copy(plot = null) to expectedDefault
            ).map { (plotHolder, expected) ->
                DynamicTest.dynamicTest("should add plot for plot holder") {
                    val newState = mainFunctionalCore(
                        state = gameState(
                            actions = listOfNotNull(plotHolder as? Action),
                            locationSelectionState = locationSelectionState(
                                allLocations = listOfNotNull(
                                    plotHolder as? Location
                                )
                            )
                        ),
                        viewAction = when (plotHolder) {
                            is Action -> MainViewAction.ActionClicked(id = plotHolder.id)
                            is Location -> MainViewAction.LocationSelected(id = plotHolder.id)
                            else -> MainViewAction.Init
                        },
                    )

                    assertThat(newState)
                        .prop(GameState::plotEntries)
                        .extracting(PlotEntry::text)
                        .containsExactly(expected)
                }
            }
        }

}
