package com.daniil.shevtsov.idle.feature.player.species.presentation

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerSpeciesModel(
    val id: Long,
    val title: String,
    val tags: List<Tag>,
    val isSelected: Boolean,
)
