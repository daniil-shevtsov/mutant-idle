package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.toFinishedGameState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.mapGameStartViewState
import com.daniil.shevtsov.idle.feature.main.presentation.drawerPresentation
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val drawerState = drawerPresentation(state)
    val contentState = when (state.currentScreen) {
        Screen.GameStart -> ScreenContentViewState.GameStart(mapGameStartViewState(state))
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
        Screen.FinishedGame -> ScreenContentViewState.FinishedGame(mapFinishedGameViewState(state.toFinishedGameState()))
    }

    return ScreenHostViewState(
        drawerState = drawerState,
        contentState = contentState,
    )
}
