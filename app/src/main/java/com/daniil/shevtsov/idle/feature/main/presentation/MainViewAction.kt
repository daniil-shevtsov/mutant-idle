package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId

sealed class MainViewAction {
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()

    data class DrawerTabSwitched(val id : DrawerTabId) : MainViewAction()
    data class DebugJobSelected(val id: Long) : MainViewAction()
    data class DebugSpeciesSelected(val id: Long) : MainViewAction()
    data class LocationSelected(val id: Long) : MainViewAction()
}
