package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun tag(name: String = "") = Tag(
    name = name
)

fun playerJob(tags: List<Tag>) = PlayerJob(
    tags = tags,
)