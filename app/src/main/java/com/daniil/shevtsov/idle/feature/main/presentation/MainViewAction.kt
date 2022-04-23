package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob

sealed class MainViewAction {
    data class ActionClicked(val id: Long) : MainViewAction()
    data class UpgradeSelected(val id: Long) : MainViewAction()
    data class ToggleSectionCollapse(val key: SectionKey) : MainViewAction()

    data class DrawerTabSwitched(val id : DrawerTabId) : MainViewAction()
    data class DebugJobSelected(val job: PlayerJob) : MainViewAction()
}
