package com.daniil.shevtsov.idle.feature.action.presentation

data class RatioChangeModel(
    val icon: String,
    val value: Double,
)

fun ratioChangeModel(
    icon: String = "",
    value: Double = 0.0,
) = RatioChangeModel(
    icon = icon,
    value = value,
)
