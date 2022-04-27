package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerJob(
    id: Long = 0L,
    title: String = "",
    tags: List<Tag> = emptyList(),
) = PlayerJob(
    id = id,
    title = title,
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
