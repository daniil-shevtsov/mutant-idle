package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

fun location(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    plot: String? = null,
    tags: Map<TagRelation, List<Tag>> = mapOf(),
) = Location(
    id = id,
    title = title,
    description = description,
    plot = plot,
    tags = tags,
)
