package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.toFinishedGameState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.main.domain.toMainState
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenViewState {
    return when (state.currentScreen) {
        Screen.Main -> ScreenViewState.Main(mapMainViewState(state.toMainState()))
        Screen.FinishedGame -> ScreenViewState.FinishedGame(mapFinishedGameViewState(state.toFinishedGameState()))
    }
}
