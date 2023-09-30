package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Unlock(
    val id: Long,
    val title: String,
    val description: String,
    val newTags: List<Tag>,
)

fun unlock(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    newTags: List<Tag> = emptyList(),
) = Unlock(
    id = id,
    title = title,
    description = description,
    newTags = newTags,
)
