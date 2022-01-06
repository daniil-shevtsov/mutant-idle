package com.daniil.shevtsov.idle.feature.main.presentation

sealed class MainViewAction {
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
    object ToggleResourcesCollapse : MainViewAction()
    object ToggleActionsCollapse : MainViewAction()
    object ToggleUpgradesCollapse : MainViewAction()
}
