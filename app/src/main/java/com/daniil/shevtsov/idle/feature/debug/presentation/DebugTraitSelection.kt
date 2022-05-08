package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait

data class DebugTraitSelection(
    val title: String,
    val selection: List<PlayerTrait>,
)
