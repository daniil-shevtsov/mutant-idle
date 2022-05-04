package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.gamefinish.presentation.FinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState

sealed class ScreenViewState {
    data class GameStart(val state: GameStartViewState) : ScreenViewState()
    data class Main(val state: MainViewState) : ScreenViewState()
    data class FinishedGame(val state: FinishedGameViewState) : ScreenViewState()
}
