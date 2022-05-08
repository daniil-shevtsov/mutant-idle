package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun mapGameStartViewState(
    state: GameState
): GameStartViewState {
    return GameStartViewState(
        title = "Mutant Idle",
        description = "Choose species and job",
        speciesSelection = state.speciesTraits.map { species ->
            with(species) {
                TraitSelectionItem(
                    id = id,
                    title = title,
                    description = description,
                    icon = icon,
                    isSelected = species.id == state.player.species.id,
                    isUnlocked = state.unlockState.species[species.id] == true,
                )
            }
        },
        jobSelection = state.jobTraits.map { job ->
            with(job) {
                TraitSelectionItem(
                    id = id,
                    title = title,
                    description = description,
                    icon = Icons.Job,
                    isSelected = job.id == state.player.traits[TraitId.Job]?.id,
                    isUnlocked = state.unlockState.jobs[job.id] == true,
                )
            }
        },
    )
}
