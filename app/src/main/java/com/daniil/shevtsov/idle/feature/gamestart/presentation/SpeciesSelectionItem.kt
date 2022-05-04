package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class SpeciesSelectionItem(
    val id: Long,
    val title: String,
    val description: String,
    val icon: String,
)

fun speciesSelectionItem(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    icon: String = "",
) = SpeciesSelectionItem(
    id = id,
    title = title,
    description = description,
    icon = icon,
)
