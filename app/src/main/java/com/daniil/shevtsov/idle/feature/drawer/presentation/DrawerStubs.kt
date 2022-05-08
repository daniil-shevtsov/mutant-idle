package com.daniil.shevtsov.idle.feature.drawer.presentation

import com.daniil.shevtsov.idle.core.ui.debugViewState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel

fun drawerTab(
    id: DrawerTabId = DrawerTabId.PlayerInfo,
    title: String = "",
    isSelected: Boolean = false,
) = DrawerTab(
    id = id,
    title = title,
    isSelected = isSelected,
)

fun drawerViewState(
    tabSelectorState: List<DrawerTab> = emptyList(),
    drawerContent: DrawerContentViewState = drawerDebugContent(),
) = DrawerViewState(
    tabSelectorState = tabSelectorState,
    drawerContent = drawerContent,
)

fun drawerDebugContent(
    jobSelection: List<PlayerJobModel> = emptyList(),
) = DrawerContentViewState.Debug(
    state = debugViewState(
        jobSelection = jobSelection,
    ),
)
