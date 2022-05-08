package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.toSpecies
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerSpecies(
    id: Long = 0L,
    title: String = "",
    icon: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
) = playerTrait(
    id = id,
    title = title,
    icon = icon,
    description = description,
    tags = tags,
).toSpecies()

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
