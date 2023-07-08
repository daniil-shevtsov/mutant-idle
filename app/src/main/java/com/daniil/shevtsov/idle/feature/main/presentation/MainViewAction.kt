package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.menu.presentation.MenuId

sealed class MainViewAction {
    object Init : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()

    object LocationSelectionExpandChange : MainViewAction()
    data class SelectableClicked(val id: Long) : MainViewAction()

    object StartNewGameClicked : MainViewAction()

    data class MenuButtonClicked(val id: MenuId) : MainViewAction()
}
