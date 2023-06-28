package com.daniil.shevtsov.idle.feature.location.domain

data class LocationSelectionState(
    val selectedLocation: Location,
    val isSelectionExpanded: Boolean,
)

fun locationSelectionState(
    selectedLocation: Location = location(),
    isSelectionExpanded: Boolean = false,
) = LocationSelectionState(
    selectedLocation = selectedLocation,
    isSelectionExpanded = isSelectionExpanded,
)
