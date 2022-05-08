package com.daniil.shevtsov.idle.feature.player.trait.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerTrait(
    val id: Long,
    val traitId: TraitId,
    val title: String,
    val description: String,
    val tags: List<Tag>,
    val icon: String? = null,
)

fun PlayerTrait.toSpecies() = PlayerSpecies(
    id = id,
    icon = icon ?: Icons.TraitDefault,
    title = title,
    description = description,
    tags = tags,
)

fun PlayerTrait.toJob() = PlayerJob(
    id = id,
    title = title,
    description = description,
    tags = tags,
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
