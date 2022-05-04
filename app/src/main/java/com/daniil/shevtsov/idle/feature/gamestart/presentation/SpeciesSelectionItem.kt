package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class SpeciesSelectionItem(
    val id: Long,
    val title: String,
    val description: String,
    val icon: String,
    val isSelected: Boolean,
)

fun speciesSelectionItem(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    icon: String = "",
    isSelected: Boolean = false,
) = SpeciesSelectionItem(
    id = id,
    title = title,
    description = description,
    icon = icon,
    isSelected = isSelected,
)
