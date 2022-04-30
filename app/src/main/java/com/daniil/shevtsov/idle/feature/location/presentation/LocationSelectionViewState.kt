package com.daniil.shevtsov.idle.feature.location.presentation

data class LocationSelectionViewState(
    val locations: List<LocationModel>,
    val selectedLocation: LocationModel,
    val isExpanded: Boolean,
)

fun locationSelectionViewState(
    locations: List<LocationModel> = emptyList(),
    selectedLocation: LocationModel = locationModel(),
    isExpanded: Boolean = false,
) = LocationSelectionViewState(
    locations = locations,
    selectedLocation = selectedLocation,
    isExpanded = isExpanded,
)
