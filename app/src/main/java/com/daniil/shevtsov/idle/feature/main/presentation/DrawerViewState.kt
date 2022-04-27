package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab

data class DrawerViewState(
    val tabSelectorState: List<DrawerTab>,
    val drawerContent: DrawerContentViewState,
)
