package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.main.domain.handleDrawerTabSwitched

fun drawerFunctionalCore(
    state: GameState,
    viewAction: DrawerViewAction
): GameState {
    return when (viewAction) {
        is DrawerViewAction.TabSwitched -> handleDrawerTabSwitched(
            state = state,
            viewAction = viewAction,
        )
        is DrawerViewAction.Debug -> when (val debugAction = viewAction.action) {
            is DebugViewAction.JobSelected -> handleDebugJobSelected(
                state = state,
                viewAction = debugAction,
            )
            is DebugViewAction.SpeciesSelected -> handleSpeciesSelected(
                state = state,
                viewAction = debugAction,
            )
            is DebugViewAction.UnlockEverything -> unlockEverything(
                state = state,
                viewAction = debugAction,
            )
        }
    }
}

fun handleDebugJobSelected(
    state: GameState,
    viewAction: DebugViewAction.JobSelected
): GameState {
    val newJob = state.availableJobs.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            job = newJob,
        )
    )

}

private fun handleSpeciesSelected(
    state: GameState,
    viewAction: DebugViewAction.SpeciesSelected
): GameState {
    val newSpecies = state.availableSpecies.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            species = newSpecies,
        )
    )
}

private fun unlockEverything(
    state: GameState,
    viewAction: DebugViewAction.UnlockEverything
) = state.copy(
    unlockState = state.unlockState.copy(
        species = state.availableSpecies.map { species -> species.id to true }.toMap(),
        jobs = state.availableJobs.map { job -> job.id to true }.toMap(),
    )
)