package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun mapGameStartViewState(
    state: GameState
): GameStartViewState {
    return GameStartViewState(
        title = "Mutant Idle",
        description = "Choose species",
        speciesSelection = state.availableSpecies.map { species ->
            with(species) {
                SpeciesSelectionItem(
                    id = id,
                    title = title,
                    description = description,
                    icon = icon,
                    isSelected = species.id == state.player.species.id,
                    isUnlocked = state.unlockState.species[species.id] == true,
                )
            }
        }
    )
}
