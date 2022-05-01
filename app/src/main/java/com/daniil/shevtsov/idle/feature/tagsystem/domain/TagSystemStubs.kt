package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun tag(
    name: String = "",
    description: String? = null,
) = Tag(
    name = name,
    description = description,
)
