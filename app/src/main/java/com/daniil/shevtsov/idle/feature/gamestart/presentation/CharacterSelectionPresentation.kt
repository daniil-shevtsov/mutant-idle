package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun mapCharacterSelectionViewState(
    state: GameState
): CharacterSelectionViewState {
    return CharacterSelectionViewState(
        title = "Mutant Idle",
        description = "Choose species and job",
        speciesSelection = state.availableTraits
            .filter { trait -> trait.traitId == TraitId.Species }
            .map { species ->
                with(species) {
                    TraitSelectionItem(
                        id = id,
                        title = title,
                        description = description,
                        icon = icon,
                        isSelected = species.id == state.player.traits[TraitId.Species]?.id,
                        isUnlocked = state.unlockState.traits[TraitId.Species]?.get(species.id) == true,
                    )
                }
            },
        jobSelection = state.availableTraits
            .filter { trait -> trait.traitId == TraitId.Job }
            .map { job ->
                with(job) {
                    TraitSelectionItem(
                        id = id,
                        title = title,
                        description = description,
                        icon = Icons.Job,
                        isSelected = job.id == state.player.traits[TraitId.Job]?.id,
                        isUnlocked = state.unlockState.traits[TraitId.Job]?.get(job.id) == true,
                    )
                }
            },
    )
}
