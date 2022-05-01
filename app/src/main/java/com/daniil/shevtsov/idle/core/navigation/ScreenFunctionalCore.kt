package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun generalFunctionalCore(
    state: GameState,
    viewAction: GeneralViewAction,
): GameState = when (viewAction) {
    is GeneralViewAction.Open -> state.copy(currentScreen = viewAction.screen)
    is GeneralViewAction.Back -> state
}
