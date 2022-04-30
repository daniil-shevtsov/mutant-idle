package com.daniil.shevtsov.idle.feature.location.presentation

data class LocationModel(
    val id: Long,
    val title: String,
    val description: String,
    val isSelected: Boolean,
)

fun locationModel(
    id: Long = 0L,
    title: String = "",
    description: String = "",
    isSelected: Boolean = false,
) = LocationModel(
    id = id,
    title = title,
    description = description,
    isSelected = isSelected,
)
