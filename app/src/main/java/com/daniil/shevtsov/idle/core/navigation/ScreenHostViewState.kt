package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.main.presentation.DrawerViewState

data class ScreenHostViewState(
    val drawerState: DrawerViewState,
    val contentState: ScreenContentViewState,
)
