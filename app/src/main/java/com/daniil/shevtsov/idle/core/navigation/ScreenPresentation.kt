package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState

fun screenPresentationFunctionalCore(
    state: MainFunctionalCoreState
): ScreenViewState {
    return when (state.currentScreen) {
        Screen.Main -> ScreenViewState.Main(mapMainViewState(state))
        Screen.FinishedGame -> ScreenViewState.FinishedGame(mapFinishedGameViewState(state))
    }
}
