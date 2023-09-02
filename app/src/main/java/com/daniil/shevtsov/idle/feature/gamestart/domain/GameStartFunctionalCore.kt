package com.daniil.shevtsov.idle.feature.gamestart.domain

import com.daniil.shevtsov.idle.core.navigation.MishaEffect
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import com.daniil.shevtsov.idle.feature.player.core.domain.PlayerViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.playerFunctionalCore
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

typealias FunctionalCoreResult = Pair<GameState, List<MishaEffect>>

fun functionalCoreResult(
    state: GameState,
    effects: List<MishaEffect> = emptyList()
): FunctionalCoreResult = state to effects

fun gameStartFunctionalCore(
    state: GameState,
    viewAction: GameStartViewAction,
): FunctionalCoreResult {
    return when (viewAction) {
        is GameStartViewAction.Init -> handleTitleRequested(
            state = state,
            viewAction = viewAction,
        )

        is GameStartViewAction.StartGame -> handleStartGame(
            state = state,
            viewAction = viewAction,
        ) to emptyList()

        is GameStartViewAction.TraitSelected -> handleTraitSelected(
            state = state,
            viewAction = viewAction,
        ) to emptyList()

        is GameStartViewAction.TitleReceived -> handleResultReceived(
            state = state,
            viewAction = viewAction,
        )

        GameStartViewAction.CancelClicked -> handleCancelClicked(
            state = state,
            viewAction = viewAction,
        )
    }
}

fun handleCancelClicked(
    state: GameState,
    viewAction: GameStartViewAction
): FunctionalCoreResult {
    return functionalCoreResult(state, effects = listOf(MishaEffect.CancelRequestingTitle))
}

fun handleResultReceived(
    state: GameState,
    viewAction: GameStartViewAction.TitleReceived
): FunctionalCoreResult {
    return functionalCoreResult(
        state = state.copy(
            gameTitle = MenuTitleState.Result(text = viewAction.title)
        )
    )
}

fun handleTitleRequested(
    state: GameState,
    viewAction: GameStartViewAction.Init
): FunctionalCoreResult {
    return state to listOf(MishaEffect.RequestTitleFromServer)
}

private fun handleTraitSelected(
    state: GameState,
    viewAction: GameStartViewAction.TraitSelected,
): GameState {
    val newTrait =
        state.availableTraits.find { it.traitId == viewAction.traitId && it.id == viewAction.id }!!

    val unlockStatus = when (newTrait.traitId) {
        TraitId.Species -> state.unlockState.traits[TraitId.Species]
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
