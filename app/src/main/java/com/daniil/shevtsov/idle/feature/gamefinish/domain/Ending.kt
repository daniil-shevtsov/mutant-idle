package com.daniil.shevtsov.idle.feature.gamefinish.domain

data class Ending(
    val id: Long,
    val title: String,
    val description: String,
)

fun ending(
    id: Long = 0L,
    title: String = "",
    description: String = "",
) = Ending(
    id = id,
    title = title,
    description = description,
)
