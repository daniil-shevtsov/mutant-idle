package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations
import com.daniil.shevtsov.idle.feature.tagsystem.domain.noTagRelations

fun location(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    plot: String? = null,
    tagRelations: TagRelations = noTagRelations(),
) = Location(
    id = id,
    title = title,
    subtitle = subtitle,
    plot = plot,
    tagRelations = tagRelations,
)
