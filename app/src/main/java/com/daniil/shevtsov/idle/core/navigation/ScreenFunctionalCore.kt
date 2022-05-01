package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.main.domain.toMainState
import com.daniil.shevtsov.idle.feature.main.domain.updateGameState
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey

fun screenFunctionalCore(
    state: GameState,
    viewAction: ScreenViewAction,
): GameState {
    return when (viewAction) {
        is ScreenViewAction.Main -> mainFunctionalCore(
            state = state.toMainState(),
            viewAction = viewAction.action,
        ).updateGameState(currentState = state) //TODO: Find a way to do such thing in a not hacky way
            .let { newGameState ->
                if(newGameState.ratios.find { it.key == RatioKey.Suspicion }?.value ?: 0.0 >= 1.0) {
                    generalFunctionalCore(
                        state = newGameState,
                        viewAction = GeneralViewAction.Open(screen = Screen.FinishedGame),
                    )
                } else {
                    newGameState
                }
            }
        is ScreenViewAction.General -> generalFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
    }
}
