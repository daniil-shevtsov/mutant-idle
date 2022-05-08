package com.daniil.shevtsov.idle.feature.main.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerContentViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewState
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun drawerPresentation(
    state: GameState
): DrawerViewState {

    return DrawerViewState(
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
                        speciesSelection = state.availableTraits
                            .filter { trait -> trait.traitId == TraitId.Species }
                            .map { species ->
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
}
