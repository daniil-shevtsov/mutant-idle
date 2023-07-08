package com.daniil.shevtsov.idle.feature.menu.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import kotlinx.collections.immutable.persistentListOf

fun mapMenuViewState(
    state: GameState
): MenuViewState {
    return MenuViewState(
        title = "Mutant Idle",
        buttons = persistentListOf(
            MenuButtonModel(id = 0L, title = "Start Game"),
            MenuButtonModel(id = 1L, title = "Settings"),
            MenuButtonModel(id = 2L, title = "Quit"),
        )
    )
}
