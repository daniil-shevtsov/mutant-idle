package com.daniil.shevtsov.idle.feature.drawer.presentation

fun drawerTab(
    id: DrawerTabId = DrawerTabId.PlayerInfo,
    title: String = "",
    isSelected: Boolean = false,
) = DrawerTab(
    id = id,
    title = title,
    isSelected = isSelected,
)