package com.daniil.shevtsov.idle.feature.menu.presentation

import kotlinx.collections.immutable.ImmutableList

data class MenuViewState(
    val title: String,
    val buttons: ImmutableList<MenuButtonModel>,
)
