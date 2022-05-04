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
        currentScreen = Screen.Main,
        screenStack = listOf(Screen.Main),
    )
}
