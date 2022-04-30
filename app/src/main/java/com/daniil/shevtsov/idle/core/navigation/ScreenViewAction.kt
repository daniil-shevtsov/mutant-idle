package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction

sealed class ScreenViewAction {
    data class Main( val action: MainViewAction) : ScreenViewAction()
}
