package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun generalFunctionalCore(
    state: GameState,
    viewAction: GeneralViewAction,
): GameState = when (viewAction) {
    is GeneralViewAction.Open -> state.copy(
        currentScreen = viewAction.screen,
        screenStack = state.screenStack + listOf(viewAction.screen)
    )
    is GeneralViewAction.Back -> {
        if(state.screenStack.size > 1) {
            val newScreenStack = state.screenStack.dropLast(1)
            val newCurrentScreen = newScreenStack.last()
            state.copy(
                currentScreen = newCurrentScreen,
                screenStack = newScreenStack,
            )
        } else {
            state
        }
    }
}
