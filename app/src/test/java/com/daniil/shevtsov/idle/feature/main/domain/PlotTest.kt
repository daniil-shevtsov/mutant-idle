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
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class PlotTest() {

    private data class PlotTestData(
        val plotHolder: PlotHolder,
        val selectAction: MainViewAction,
        val expectedDefaultPlot: String,
    )

    private val plotHolders = listOf(
        PlotTestData(
            plotHolder = action(
                id = 1L,
                title = "test action",
                plot = "action plot"
            ),
            selectAction = MainViewAction.ActionClicked(id = 1L),
            expectedDefaultPlot = "You performed action \"test action\""
        ),
        PlotTestData(
            plotHolder = location(
                id = 2L,
                title = "test location",
                plot = "location plot"
            ),
            selectAction = MainViewAction.LocationSelected(id = 2L),
            expectedDefaultPlot = "You went to the test location"
        ),
        PlotTestData(
            plotHolder = upgrade(
                id = 3L,
                title = "test upgrade",
                plot = "upgrade plot"
            ),
            selectAction = MainViewAction.UpgradeSelected(id = 3L),
            expectedDefaultPlot = "You have gained an upgrade \"test upgrade\""
        ),
    )

    @TestFactory
    fun testPlotHolders() = plotHolders
        .flatMap { plotTestData ->
            listOf(
                plotTestData.plotHolder to plotTestData.plotHolder.plot,
                plotTestData.plotHolder.copy(plot = null) to plotTestData.expectedDefaultPlot
            ).map { (plotHolder, expected) ->
                val testName = when (plotHolder.plot) {
                    null -> "when holder without plot selected - then should add its default plot"
                    else -> "when holder selected - then should add its plot"
                }
                DynamicTest.dynamicTest(testName) {
                    val newState = mainFunctionalCore(
                        state = gameState(
                            actions = listOfNotNull(plotHolder as? Action),
                            upgrades = listOfNotNull(plotHolder as? Upgrade),
                            locations = listOfNotNull(
                                plotHolder as? Location
                            ),
                            locationSelectionState = locationSelectionState(
                                allLocations = listOfNotNull(
                                    plotHolder as? Location
                                )
                            )
                        ),
                        viewAction = plotTestData.selectAction,
                    )

                    assertThat(newState)
                        .prop(GameState::plotEntries)
                        .extracting(PlotEntry::text)
                        .containsExactly(expected)
                }
            }
        }

}
