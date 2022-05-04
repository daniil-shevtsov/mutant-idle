package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class SpeciesSelectionItem(
    val id: Long,
    val title: String,
    val description: String,
)

fun speciesSelectionItem(
    id: Long = 0L,
    title: String = "",
    description: String = "",
) = SpeciesSelectionItem(
    id = id,
    title = title,
    description = description,
)
