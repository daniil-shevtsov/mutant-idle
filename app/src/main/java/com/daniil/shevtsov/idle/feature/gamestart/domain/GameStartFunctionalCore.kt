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
        is GameStartViewAction.TraitSelected -> handleTraitSelected(
            state = state,
            viewAction = viewAction,
        )
    }
}

private fun handleSpeciesSelected(
    state: GameState,
    viewAction: GameStartViewAction.SpeciesSelected,
): GameState {
    val newTrait = state.availableTraits.find { it.traitId == TraitId.Species && it.id == viewAction.id }!!
    val newTraits = state.player.traits.toMutableMap().apply { put(TraitId.Species, newTrait) }.toMap()

    return when {
        state.unlockState.species[newTrait.id] == true -> state.copy(
            player = state.player.copy(
                traits = newTraits,
            ),
        ).let { state ->
            playerFunctionalCore(
                state = state,
                action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
            )
        }
        else -> state
    }
}

private fun handleJobSelected(
    state: GameState,
    viewAction: GameStartViewAction.JobSelected,
): GameState {
    val newTrait = state.availableTraits.find { it.traitId == TraitId.Job && it.id == viewAction.id }!!
    val newTraits = state.player.traits.toMutableMap().apply { put(TraitId.Job, newTrait) }.toMap()

    return when {
        state.unlockState.jobs[newTrait.id] == true -> state.copy(
            player = state.player.copy(
                traits = newTraits,
            ),
        ).let { state ->
            playerFunctionalCore(
                state = state,
                action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
            )
        }
        else -> state
    }
}

private fun handleTraitSelected(
    state: GameState,
    viewAction: GameStartViewAction.TraitSelected,
): GameState {
    val newTrait = state.availableTraits.find { it.traitId == viewAction.traitId && it.id == viewAction.id }!!
    val newTraits = state.player.traits.toMutableMap().apply { put(viewAction.traitId, newTrait) }.toMap()

    val unlockStatus = when(newTrait.traitId) {
        TraitId.Species -> state.unlockState.species
        TraitId.Job -> state.unlockState.jobs
    }

    return when {
        unlockStatus[newTrait.id] == true -> state.copy(
            player = state.player.copy(
                traits = newTraits,
            ),
        ).let { state ->
            playerFunctionalCore(
                state = state,
                action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
            )
        }
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
