package com.daniil.shevtsov.idle.feature.gamestart.presentation

data class CharacterSelectionViewState(
    val title: String,
    val description: String,
    val speciesSelection: List<TraitSelectionItem>,
    val jobSelection: List<TraitSelectionItem>,
)

fun characterSelectionViewState(
    title: String = "",
    description: String = "",
    speciesSelection: List<TraitSelectionItem> = emptyList(),
    jobSelection: List<TraitSelectionItem> = emptyList(),
) = CharacterSelectionViewState(
    title = title,
    description = description,
    speciesSelection = speciesSelection,
    jobSelection = jobSelection,
)
