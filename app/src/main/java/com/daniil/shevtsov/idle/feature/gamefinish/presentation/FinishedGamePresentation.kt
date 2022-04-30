package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import com.daniil.shevtsov.idle.feature.gamefinish.domain.FinishedGameState

fun mapFinishedGameViewState(
    state: FinishedGameState
): FinishedGameViewState {
    return finishedGameViewState(
        endingState = EndingViewState(
            description = state.endingText,
        )
    )
}
