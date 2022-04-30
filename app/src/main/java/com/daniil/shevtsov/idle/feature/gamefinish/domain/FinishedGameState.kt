package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

data class FinishedGameState(
    val endingText: String,
)

fun GameState.toFinishedGameState()= FinishedGameState(
    endingText = "",
)

fun finishedGameState(
    endingText: String = "",
) = FinishedGameState(
    endingText = endingText,
)
