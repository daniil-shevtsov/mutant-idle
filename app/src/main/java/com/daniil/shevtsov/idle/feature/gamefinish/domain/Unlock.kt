package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Unlock(
    val title: String,
    val description: String,
    val newTags: List<Tag>,
)

fun unlock(
    title: String = "",
    description: String = "",
    newTags: List<Tag> = emptyList(),
) = Unlock(
    title = title,
    description = description,
    newTags = newTags,
)
