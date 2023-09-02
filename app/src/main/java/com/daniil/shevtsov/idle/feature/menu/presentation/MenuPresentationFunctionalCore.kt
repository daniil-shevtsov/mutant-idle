package com.daniil.shevtsov.idle.feature.menu.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import kotlinx.collections.immutable.persistentListOf

fun mapMenuViewState(
    state: GameState
): MenuViewState {
    return MenuViewState(
        title = state.gameTitle.toViewState(),
        buttons = persistentListOf(
            MenuButtonModel(id = MenuId.StartGame, title = "Start Game"),
            MenuButtonModel(id = MenuId.Settings, title = "Settings"),
        )
    )
}

private fun MenuTitleState.toViewState() = when (this) {
    MenuTitleState.Error -> MenuTitleViewState.Error
    MenuTitleState.Loading -> MenuTitleViewState.Loading
    is MenuTitleState.Result -> MenuTitleViewState.Result(text)
}

