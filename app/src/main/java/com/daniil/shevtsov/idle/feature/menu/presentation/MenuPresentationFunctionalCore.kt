package com.daniil.shevtsov.idle.feature.menu.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import kotlinx.collections.immutable.persistentListOf

fun mapMenuViewState(
    state: GameState
): MenuViewState {
    return MenuViewState(
        title = "Mutant Idle",
        buttons = persistentListOf(
            MenuButtonModel(id = MenuId.StartGame, title = "Start Game"),
            MenuButtonModel(id = MenuId.Settings, title = "Settings"),
        )
    )
}
