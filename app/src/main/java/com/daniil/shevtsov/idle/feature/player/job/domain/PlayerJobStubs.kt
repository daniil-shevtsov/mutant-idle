package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerJob(
    title: String = "",
    tags: List<Tag> = emptyList(),
) = PlayerJob(
    title = title,
    tags = tags,
)
