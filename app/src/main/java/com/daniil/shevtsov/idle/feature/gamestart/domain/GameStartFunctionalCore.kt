package com.daniil.shevtsov.idle.feature.gamestart.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction

fun gameStartFunctionalCore(
    state: GameState,
    viewAction: GameStartViewAction,
): GameState {
    return state
}
