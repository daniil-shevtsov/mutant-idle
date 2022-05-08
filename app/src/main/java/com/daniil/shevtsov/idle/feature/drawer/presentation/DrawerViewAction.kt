package com.daniil.shevtsov.idle.feature.drawer.presentation

import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction

sealed class DrawerViewAction {
    data class TabSwitched(val id: DrawerTabId) : DrawerViewAction()
    data class Debug(val action: DebugViewAction) : DrawerViewAction()
}
