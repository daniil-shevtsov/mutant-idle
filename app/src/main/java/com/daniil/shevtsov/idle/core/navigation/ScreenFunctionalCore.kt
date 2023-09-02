package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.domain.drawerFunctionalCore
import com.daniil.shevtsov.idle.feature.gamestart.domain.gameStartFunctionalCore
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore

sealed interface MishaEffect {
    object Kek : MishaEffect
}

fun screenFunctionalCore(
    state: GameState,
    viewAction: ScreenViewAction,
): Pair<GameState, List<MishaEffect>> {
    return when (viewAction) {
        is ScreenViewAction.Start -> gameStartFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )

        is ScreenViewAction.Main -> mainFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )

        is ScreenViewAction.General -> generalFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )

        is ScreenViewAction.Drawer -> drawerFunctionalCore(
            state = state,
            viewAction = viewAction.action,
        )
    } to emptyList()
}
