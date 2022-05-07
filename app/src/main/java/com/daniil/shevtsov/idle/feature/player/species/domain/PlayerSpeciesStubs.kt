package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerSpecies(
    id: Long = 0L,
    title: String = "",
    icon: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
) = PlayerSpecies(
    id = id,
    title = title,
    icon = icon,
    description = description,
    tags = tags,
)

fun playerSpeciesModel(
    id: Long = 0L,
    title: String = "",
    tags: List<Tag> = emptyList(),
    isSelected: Boolean = false,
) = PlayerSpeciesModel(
    id = id,
    title = title,
    tags = tags,
    isSelected = isSelected,
)
