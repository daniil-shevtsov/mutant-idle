package com.daniil.shevtsov.idle.feature.tagsystem.domain

data class Tag(
    val name: String,
    val description: String? = null,
)

fun tag(
    name: String = "",
    description: String? = null,
) = Tag(
    name = name,
    description = description,
)

