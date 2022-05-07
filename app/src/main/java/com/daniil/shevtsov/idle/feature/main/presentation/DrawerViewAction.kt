package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId

sealed class DrawerViewAction {
    data class TabSwitched(val id: DrawerTabId) : DrawerViewAction()
    data class Debug(val action: DebugViewAction) : DrawerViewAction()
}
