package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class GameStartViewState(
    val title: String,
    val description: String,
    val speciesSelection: List<TraitSelectionItem>,
    val jobSelection: List<TraitSelectionItem>,
)

fun gameStartViewState(
    title: String = "",
    description: String = "",
    speciesSelection: List<TraitSelectionItem> = emptyList(),
    jobSelection: List<TraitSelectionItem> = emptyList(),
) = GameStartViewState(
    title = title,
    description = description,
    speciesSelection = speciesSelection,
    jobSelection = jobSelection,
)
