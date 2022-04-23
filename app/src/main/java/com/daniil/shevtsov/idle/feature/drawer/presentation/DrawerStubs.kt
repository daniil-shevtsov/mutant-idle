package com.daniil.shevtsov.idle.feature.drawer.presentation

fun drawerTab(
    id: DrawerTabId = DrawerTabId.PlayerInfo,
    title: String = "",
) = DrawerTab(
    id = id,
    title = title,
)