package com.daniil.shevtsov.idle.feature.player.trait.domain

import com.daniil.shevtsov.idle.feature.colors.presentation.SpeciesColors
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerTrait(
    val id: Long,
    val traitId: TraitId,
    val title: String,
    val description: String,
    val tags: List<Tag>,
    val icon: String? = null,
    val mainRatio: RatioKey? = null,
    val mainResource: ResourceKey? = null,
    val colors: SpeciesColors? = null,
)

fun playerTrait(
    id: Long = 0L,
    traitId: TraitId = TraitId.Job,
    title: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
    icon: String? = null,
    mainRatio: RatioKey? = null,
    mainResource: ResourceKey? = null,
    colors: SpeciesColors? = null,
) = PlayerTrait(
    id = id,
    traitId = traitId,
    title = title,
    description = description,
    tags = tags,
    icon = icon,
    mainRatio = mainRatio,
    mainResource = mainResource,
    colors = colors,
)
