package com.daniil.shevtsov.idle.feature.location.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Location(
    val id: Long,
    val title: String,
    val description: String,
    val plot: String,
    val tags: Map<TagRelation, List<Tag>>,
)
