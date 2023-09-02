package com.daniil.shevtsov.idle.feature.menu.presentation

import kotlinx.collections.immutable.ImmutableList

data class MenuViewState(
    val title: MenuTitleViewState,
    val buttons: ImmutableList<MenuButtonModel>,
)

sealed interface MenuTitleState {
    object Loading : MenuTitleState
    data class Result(val text: String) : MenuTitleState
    object Error : MenuTitleState
}

sealed interface MenuTitleViewState {
    object Loading : MenuTitleViewState
    data class Result(val text: String) : MenuTitleViewState
    object Error : MenuTitleViewState
}
