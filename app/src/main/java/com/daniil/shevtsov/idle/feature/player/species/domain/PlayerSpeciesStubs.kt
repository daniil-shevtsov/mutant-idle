package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerSpecies(
    id: Long = 0L,
    title: String = "",
    icon: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
    mainRatio: RatioKey? = null,
    mainResource: ResourceKey? = null,
) = playerTrait(
    id = id,
    title = title,
    traitId = TraitId.Species,
    icon = icon,
    description = description,
    tags = tags,
    mainRatio =  mainRatio,
    mainResource = mainResource,
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
