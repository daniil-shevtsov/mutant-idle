package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.action.domain.TagRelations

fun location(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    plot: String? = null,
    tags: TagRelations = mapOf(),
) = Location(
    id = id,
    title = title,
    subtitle = subtitle,
    plot = plot,
    tags = tags,
)
