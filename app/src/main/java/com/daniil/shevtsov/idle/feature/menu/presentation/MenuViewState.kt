package com.daniil.shevtsov.idle.feature.menu.presentation

import kotlinx.collections.immutable.ImmutableList

data class MenuViewState(
    val title: String,
    val newTitle: MenuTitleViewState,
    val buttons: ImmutableList<MenuButtonModel>,
)

sealed interface MenuTitleViewState {
    object Loading : MenuTitleViewState
    data class Result(val text: String) : MenuTitleViewState
    object Error : MenuTitleViewState
}
