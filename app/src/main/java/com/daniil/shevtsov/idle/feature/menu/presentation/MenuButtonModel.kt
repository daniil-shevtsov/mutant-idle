package com.daniil.shevtsov.idle.feature.menu.presentation

data class MenuButtonModel(
    val id: MenuId,
    val title: String,
)

fun menuButtonModel(
    id: MenuId = MenuId.Quit,
    title: String = "",
) = MenuButtonModel(
    id = id,
    title = title,
)

enum class MenuId {
    StartGame,
    Settings,
    Quit,
}
