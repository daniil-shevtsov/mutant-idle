package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.gamefinish.domain.toFinishedGameState
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.mapGameStartViewState
import com.daniil.shevtsov.idle.feature.main.presentation.DrawerContentViewState
import com.daniil.shevtsov.idle.feature.main.presentation.DrawerViewState
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val drawerState = DrawerViewState(
        tabSelectorState = state.drawerTabs,
        drawerContent = when (state.drawerTabs.find { it.isSelected }?.id
            ?: DrawerTabId.PlayerInfo) {
            DrawerTabId.Debug -> {
                DrawerContentViewState.Debug(
                    state = DebugViewState(
                        jobSelection = state.availableJobs.map { job ->
                            with(job) {
                                PlayerJobModel(
                                    id = id,
                                    title = title,
                                    tags = tags,
                                    isSelected = state.player.job.id == job.id,
                                )
                            }
                        },
                        speciesSelection = state.availableSpecies.map { species ->
                            with(species) {
                                PlayerSpeciesModel(
                                    id = id,
                                    title = title,
                                    tags = tags,
                                    isSelected = state.player.species.id == species.id,
                                )
                            }
                        }
                    )
                )
            }
            DrawerTabId.PlayerInfo -> {
                DrawerContentViewState.PlayerInfo(
                    playerInfo = PlayerInfoState(
                        playerJob = state.player.job,
                        playerTags = state.player.tags,
                    )
                )
            }
        }
    )
    val contentState = when (state.currentScreen) {
        Screen.GameStart -> ScreenContentViewState.GameStart(mapGameStartViewState(state))
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
        Screen.FinishedGame -> ScreenContentViewState.FinishedGame(mapFinishedGameViewState(state.toFinishedGameState()))
    }

    return ScreenHostViewState(
        drawerState = drawerState,
        contentState = contentState,
    )
}
