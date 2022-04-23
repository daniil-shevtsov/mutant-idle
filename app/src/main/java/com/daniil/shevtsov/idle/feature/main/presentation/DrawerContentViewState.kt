package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState

sealed class DrawerContentViewState {
    data class Debug(val state: DebugViewState): DrawerContentViewState()
    data class PlayerInfo(val playerInfo: PlayerInfoState) : DrawerContentViewState()
}