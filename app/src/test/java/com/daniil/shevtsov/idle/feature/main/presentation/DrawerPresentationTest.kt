package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugTraitSelection
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.*
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.toPlayerTrait
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class DrawerPresentationTest {

    @Test
    fun `should show debug job selection if has any`() = runBlockingTest {
        val state = gameState(
            drawerTabs = listOf(drawerTab(id = DrawerTabId.Debug, isSelected = true)),
            availableTraits = listOf(
                playerJob(id = 0L),
                playerJob(id = 1L),
                playerJob(id = 2L),
            )
        )

        val viewState = drawerPresentation(state = state)

        assertThat(viewState)
            .extractingDebugState()
            .extractingTraitSelections()
            .index(0)
            .prop(DebugTraitSelection::traits)
            .extracting(PlayerTrait::id)
            .containsExactly(0L, 1L, 2L)
    }

    @Test
    fun `should show debug species selection if has any`() = runBlockingTest {
        val state = gameState(
            drawerTabs = listOf(drawerTab(id = DrawerTabId.Debug, isSelected = true)),
            availableTraits = listOf(
                playerSpecies(id = 0L),
                playerSpecies(id = 1L),
                playerSpecies(id = 2L),
            ),
        )

        val viewState = drawerPresentation(state = state)

        assertThat(viewState)
            .extractingDebugState()
            .extractingTraitSelections()
            .index(0)
            .prop(DebugTraitSelection::traits)
            .extracting(PlayerTrait::id)
            .containsExactly(0L, 1L, 2L)
    }

    private fun Assert<DrawerViewState>.extractingTabs() = prop(DrawerViewState::tabSelectorState)

    private fun Assert<DrawerViewState>.extractingContentState() =
        prop(DrawerViewState::drawerContent)

    private fun Assert<DrawerViewState>.extractingDebugState() =
        prop(DrawerViewState::drawerContent)
            .isInstanceOf(DrawerContentViewState.Debug::class)
            .prop(DrawerContentViewState.Debug::state)

    private fun Assert<DebugViewState>.extractingJobSelection() = prop(DebugViewState::jobSelection)
    private fun Assert<DebugViewState>.extractingSpeciesSelection() =
        prop(DebugViewState::speciesSelection)

    private fun Assert<DebugViewState>.extractingTraitSelections() =
        prop(DebugViewState::traitSelections)
}
