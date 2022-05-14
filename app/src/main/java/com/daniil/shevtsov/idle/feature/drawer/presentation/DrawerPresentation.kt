package com.daniil.shevtsov.idle.feature.drawer.presentation

import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugTraitSelection
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
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
                        jobSelection = state.availableTraits
                            .filter { trait -> trait.traitId == TraitId.Job }
                            .map { job ->
                                with(job) {
                                    PlayerJobModel(
                                        id = id,
                                        title = title,
                                        tags = tags,
                                        isSelected = state.player.traits[TraitId.Job]?.id == job.id,
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
                                        isSelected = state.player.traits[TraitId.Species]?.id == species.id,
                                    )
                                }
                            },
                        traitSelections = TraitId.values()
                            .filter { traitId -> traitId in state.availableTraits.map(PlayerTrait::traitId) }
                            .map { traitId ->
                                val traits = state.availableTraits.filter { it.traitId == traitId }
                                DebugTraitSelection(
                                    title = formatEnumName(traitId.name),
                                    traits = traits,
                                    selectedTraitId = state.player.traits[traitId]?.id
                                        ?: traits.first().id,
                                )
                            }),
                )
            }
            DrawerTabId.PlayerInfo -> {
                DrawerContentViewState.PlayerInfo(
                    playerInfo = PlayerInfoState(
                        playerTraits = state.player.traits.values.toList(),
                        playerTags = state.player.tags,
                    )
                )
            }
        }
    )
}
