package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.presentation.TraitSelectionModel

data class DebugTraitSelection(
    val title: String,
    val traits: List<TraitSelectionModel>,
)
