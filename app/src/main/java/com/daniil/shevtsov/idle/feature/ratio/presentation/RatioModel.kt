package com.daniil.shevtsov.idle.feature.ratio.presentation

import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey

data class RatioModel(
    val key: RatioKey,
    val title: String,
    val name: String,
    val percent: Double,
    val percentLabel: String,
    val icon: String,
)

fun ratioModel(
    key: RatioKey = RatioKey.Mutanity,
    title: String = "",
    name: String = "",
    percent: Double = 0.0,
    percentLabel: String = "",
    icon: String = "",
) = RatioModel(
    key = key,
    title = title,
    name = name,
    percent = percent,
    percentLabel = percentLabel,
    icon = icon,
)
