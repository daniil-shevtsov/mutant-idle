package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState

fun mapFinishedGameViewState(
    state: MainFunctionalCoreState
): FinishedGameViewState {
    return finishedGameViewState()
}
