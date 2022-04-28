package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun playerSpecies(
    id: Long = 0L,
    title: String = "",
    tags: List<Tag> = emptyList(),
) = PlayerSpecies(
    id = id,
    title = title,
    tags = tags,
)
