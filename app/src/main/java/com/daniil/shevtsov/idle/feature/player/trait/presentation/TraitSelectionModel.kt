package com.daniil.shevtsov.idle.feature.player.trait.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait

data class TraitSelectionModel(
    val trait: PlayerTrait,
    val isSelected: Boolean,
)
