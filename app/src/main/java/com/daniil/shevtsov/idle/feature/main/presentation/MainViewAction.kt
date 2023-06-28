package com.daniil.shevtsov.idle.feature.main.presentation

sealed class MainViewAction {
    object Init : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()

    object LocationSelectionExpandChange : MainViewAction()
    data class SelectableClicked(val id: Long) : MainViewAction()
}
