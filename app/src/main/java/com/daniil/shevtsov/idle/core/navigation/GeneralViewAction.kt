package com.daniil.shevtsov.idle.core.navigation

sealed class GeneralViewAction {
    data class Open(
        val screen: Screen,
        val shouldReplace: Boolean = false,
        ) : GeneralViewAction()
    object Back : GeneralViewAction()
}
