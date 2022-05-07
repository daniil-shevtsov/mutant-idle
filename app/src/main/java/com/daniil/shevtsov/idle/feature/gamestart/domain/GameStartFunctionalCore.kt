package com.daniil.shevtsov.idle.feature.gamestart.domain

import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction

fun gameStartFunctionalCore(
    state: GameState,
    viewAction: GameStartViewAction,
): GameState {
    return when (viewAction) {
        is GameStartViewAction.SpeciesSelected -> handleSpeciesSelected(
            state = state,
            viewAction = viewAction,
        )
        is GameStartViewAction.JobSelected -> handleJobSelected(
            state = state,
            viewAction = viewAction,
        )
        is GameStartViewAction.StartGame -> handleStartGame(
            state = state,
            viewAction = viewAction,
        )
    }
}

private fun handleSpeciesSelected(
    state: GameState,
    viewAction: GameStartViewAction.SpeciesSelected,
): GameState {
    val newSpecies = state.availableSpecies.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            species = newSpecies,
        ),
    )
}

private fun handleJobSelected(
    state: GameState,
    viewAction: GameStartViewAction.JobSelected,
): GameState {
    val newJob = state.availableJobs.find { it.id == viewAction.id }!!

    return state.copy(
        player = state.player.copy(
            job = newJob,
        ),
    )
}

fun handleStartGame(
    state: GameState,
    viewAction: GameStartViewAction.StartGame
): GameState {
    return state.copy(
        currentScreen = Screen.Main,
        screenStack = listOf(Screen.Main),
    )
}
