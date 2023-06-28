package com.daniil.shevtsov.idle.feature.location.presentation

import com.daniil.shevtsov.idle.feature.upgrade.presentation.FlavoredModel

data class LocationModel(
    override val id: Long,
    override val title: String,
    override val subtitle: String,
    val isSelected: Boolean,
) : FlavoredModel

fun locationModel(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    isSelected: Boolean = false,
) = LocationModel(
    id = id,
    title = title,
    subtitle = subtitle,
    isSelected = isSelected,
)
