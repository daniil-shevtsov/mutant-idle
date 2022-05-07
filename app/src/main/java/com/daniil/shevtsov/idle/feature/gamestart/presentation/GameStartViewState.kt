package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class GameStartViewState(
    val title: String,
    val description: String,
    val speciesSelection: List<SpeciesSelectionItem>,
    val jobSelection: List<SpeciesSelectionItem>,
)

fun gameStartViewState(
    title: String = "",
    description: String = "",
    speciesSelection: List<SpeciesSelectionItem> = emptyList(),
    jobSelection: List<SpeciesSelectionItem> = emptyList(),
) = GameStartViewState(
    title = title,
    description = description,
    speciesSelection = speciesSelection,
    jobSelection = jobSelection,
)
