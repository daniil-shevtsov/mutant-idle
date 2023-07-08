package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerPresentation
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.mapGameStartViewState
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.mapMenuViewState
import com.daniil.shevtsov.idle.feature.player.species.domain.Species.Devourer
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val drawerState = drawerPresentation(state)
    val contentState = when (state.currentScreen) {
        Screen.Menu -> ScreenContentViewState.Menu(mapMenuViewState(state))
        Screen.GameStart -> ScreenContentViewState.GameStart(mapGameStartViewState(state))
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
        Screen.FinishedGame -> ScreenContentViewState.FinishedGame(mapFinishedGameViewState(state))
    }

    return ScreenHostViewState(
        speciesId = state.player.traits[TraitId.Species]?.id ?: Devourer.id,
        drawerState = drawerState,
        contentState = contentState,
    )
}
