package com.daniil.shevtsov.idle.feature.gamestart.domain

import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.PlayerViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.playerFunctionalCore
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun gameStartFunctionalCore(
    state: GameState,
    viewAction: GameStartViewAction,
): GameState {
    return when (viewAction) {
        is GameStartViewAction.StartGame -> handleStartGame(
            state = state,
            viewAction = viewAction,
        )
        is GameStartViewAction.TraitSelected -> handleTraitSelected(
            state = state,
            viewAction = viewAction,
        )
    }
}

private fun handleTraitSelected(
    state: GameState,
    viewAction: GameStartViewAction.TraitSelected,
): GameState {
    val newTrait =
        state.availableTraits.find { it.traitId == viewAction.traitId && it.id == viewAction.id }!!

    val unlockStatus = when (newTrait.traitId) {
        TraitId.Species -> state.unlockState.species
        TraitId.Job -> state.unlockState.traits[TraitId.Job]
    }

    return when {
        unlockStatus?.get(newTrait.id) == true -> playerFunctionalCore(
            state = state,
            action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
        )
        else -> state
    }
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
