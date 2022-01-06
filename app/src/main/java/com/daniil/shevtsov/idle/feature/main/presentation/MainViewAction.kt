package com.daniil.shevtsov.idle.feature.main.presentation

sealed class MainViewAction {
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()
}
