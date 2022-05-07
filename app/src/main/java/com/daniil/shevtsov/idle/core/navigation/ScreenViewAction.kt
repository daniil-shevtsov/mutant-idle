package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction

sealed class ScreenViewAction {
    data class Drawer(val action: DrawerViewAction) : ScreenViewAction()

    data class Start(val action: GameStartViewAction) : ScreenViewAction()
    data class General(val action: GeneralViewAction) : ScreenViewAction()
    data class Main(val action: MainViewAction) : ScreenViewAction()
}
