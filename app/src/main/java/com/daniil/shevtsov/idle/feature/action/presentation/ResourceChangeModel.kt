package com.daniil.shevtsov.idle.feature.action.presentation

data class ResourceChangeModel(
    val icon: String,
    val value: Double,
)

fun resourceChangeModel(
    icon: String = "",
    value: Double = 0.0,
) = ResourceChangeModel(
    icon = icon,
    value = value,
)
