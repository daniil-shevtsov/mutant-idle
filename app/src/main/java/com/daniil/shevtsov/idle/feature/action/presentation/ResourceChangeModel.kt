package com.daniil.shevtsov.idle.feature.action.presentation

data class ResourceChangeModel(
    val icon: String,
    val value: String,
)

fun resourceChangeModel(
    icon: String = "",
    value: String = "",
) = ResourceChangeModel(
    icon = icon,
    value = value,
)
