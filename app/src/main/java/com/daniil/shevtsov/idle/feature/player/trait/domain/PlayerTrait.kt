package com.daniil.shevtsov.idle.feature.player.trait.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerTrait(
    val id: Long,
    val traitId: TraitId,
    val title: String,
    val description: String,
    val tags: List<Tag>,
    val icon: String? = null,
)

fun playerTrait(
    id: Long = 0L,
    traitId: TraitId = TraitId.Job,
    title: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
    icon: String? = null,
) = PlayerTrait(
    id = id,
    traitId = traitId,
    title = title,
    description = description,
    tags = tags,
    icon = icon,
)
