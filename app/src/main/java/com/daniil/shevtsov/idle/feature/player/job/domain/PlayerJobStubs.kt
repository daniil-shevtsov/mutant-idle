package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.toJob
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerJob(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    tags: List<Tag> = emptyList(),
) = playerTrait(
    id = id,
    traitId = TraitId.Job,
    title = title,
    description = description,
    tags = tags,
)

fun playerJobModel(
    id: Long = 0L,
    title: String = "",
    tags: List<Tag> = emptyList(),
    isSelected: Boolean = false,
) = PlayerJobModel(
    id = id,
    title = title,
    tags = tags,
    isSelected = isSelected,
)
