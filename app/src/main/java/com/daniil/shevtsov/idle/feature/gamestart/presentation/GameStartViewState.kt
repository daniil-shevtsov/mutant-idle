package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class GameStartViewState(
    val title: String,
    val description: String,
)

fun gameStartViewState(
    title: String = "",
    description: String = "",
) = GameStartViewState(
    title = title,
    description = description,
)
