package com.daniil.shevtsov.idle.feature.location.domain

data class LocationSelectionState(
    val allLocations: List<Location>,
    val selectedLocation: Location,
    val isSelectionExpanded: Boolean,
)

fun locationSelectionState(
    allLocations: List<Location> = emptyList(),
    selectedLocation: Location = location(),
    isSelectionExpanded: Boolean = false,
) = LocationSelectionState(
    allLocations = allLocations,
    selectedLocation = selectedLocation,
    isSelectionExpanded = isSelectionExpanded,
)
