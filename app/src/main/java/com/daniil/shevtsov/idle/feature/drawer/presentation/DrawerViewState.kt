package com.daniil.shevtsov.idle.feature.drawer.presentation

data class DrawerViewState(
    val tabSelectorState: List<DrawerTab>,
    val drawerContent: DrawerContentViewState,
)
