package com.daniil.shevtsov.idle.feature.main.presentation

sealed class MainViewAction {
    object Init : MainViewAction()
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()

    object LocationSelectionExpandChange : MainViewAction()
    data class LocationSelected(val id: Long) : MainViewAction()
}
