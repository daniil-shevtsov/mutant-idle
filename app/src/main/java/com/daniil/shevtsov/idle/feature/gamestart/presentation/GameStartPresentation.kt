package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun mapGameStartViewState(
    state: GameState
): GameStartViewState {
    return GameStartViewState(
        title = "",
        description = "",
        speciesSelection = state.availableSpecies.map { species ->
            with(species) {
                SpeciesSelectionItem(id = id, title = title, description = description)
            }
        }
    )
}
