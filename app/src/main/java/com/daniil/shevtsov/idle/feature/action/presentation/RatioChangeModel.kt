package com.daniil.shevtsov.idle.feature.action.presentation

data class RatioChangeModel(
    val icon: String,
    val value: String,
)

fun ratioChangeModel(
    icon: String = "",
    value: String = "",
) = RatioChangeModel(
    icon = icon,
    value = value,
)
