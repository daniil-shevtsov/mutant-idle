package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel

data class DebugViewState(
    val jobSelection: List<PlayerJobModel>,
    val speciesSelection: List<PlayerSpeciesModel>,
    val traitSelections: List<DebugTraitSelection>,
)
