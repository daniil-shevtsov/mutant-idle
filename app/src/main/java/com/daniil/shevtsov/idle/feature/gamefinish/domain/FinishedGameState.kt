package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

data class FinishedGameState(
    val endings: List<Ending>,
)

fun GameState.toFinishedGameState()= FinishedGameState(
    endings = availableEndings
)

fun finishedGameState(
    endings: List<Ending>,
) = FinishedGameState(
    endings = endings,
)
