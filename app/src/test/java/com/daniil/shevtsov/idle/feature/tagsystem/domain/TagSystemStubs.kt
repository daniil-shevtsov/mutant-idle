package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob

fun tag(name: String = "") = Tag(
    name = name
)

fun playerJob(tags: List<Tag>) = PlayerJob(
    tags = tags,
)
