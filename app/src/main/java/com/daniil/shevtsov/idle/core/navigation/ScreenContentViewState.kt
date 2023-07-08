package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.gamefinish.presentation.FinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
import com.daniil.shevtsov.idle.feature.settings.view.SettingsViewState

sealed class ScreenContentViewState {
    object Loading : ScreenContentViewState()
    data class Menu(val state: MenuViewState) : ScreenContentViewState()
    data class Settings(val state: SettingsViewState) : ScreenContentViewState()
    data class GameStart(val state: GameStartViewState) : ScreenContentViewState()
    data class Main(val state: MainViewState) : ScreenContentViewState()
    data class FinishedGame(val state: FinishedGameViewState) : ScreenContentViewState()
}
