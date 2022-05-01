package com.daniil.shevtsov.idle.core.navigation

sealed class GeneralViewAction {
    data class Open(val screen: Screen) : GeneralViewAction()
    object Back : GeneralViewAction()
}
