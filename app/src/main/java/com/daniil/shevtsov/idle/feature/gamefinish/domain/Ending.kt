package com.daniil.shevtsov.idle.feature.gamefinish.domain

data class Ending(
    val id: Long,
    val title: String,
    val description: String,
    val unlocks: List<Unlock>,
)

fun ending(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    unlocks: List<Unlock> = emptyList(),
) = Ending(
    id = id,
    title = title,
    description = description,
    unlocks = unlocks,
)
