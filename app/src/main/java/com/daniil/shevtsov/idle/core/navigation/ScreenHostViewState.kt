package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewState

data class ScreenHostViewState(
    val speciesId: Long,
    val drawerState: DrawerViewState,
    val contentState: ScreenContentViewState,
)
