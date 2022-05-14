package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel

fun debugViewState(
    jobSelection: List<PlayerJobModel> = emptyList(),
    speciesSelection: List<PlayerSpeciesModel> = emptyList(),
    traitSelections: List<DebugTraitSelection> = emptyList(),
) = DebugViewState(
    jobSelection = jobSelection,
    speciesSelection = speciesSelection,
    traitSelections = traitSelections,
)
