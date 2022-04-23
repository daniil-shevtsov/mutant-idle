package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState

sealed class DrawerContentViewState {
    data class Debug(val state: DebugViewState): DrawerContentViewState()
}