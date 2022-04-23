package com.daniil.shevtsov.idle.feature.ratio.domain

fun ratio(
    key: RatioKey = RatioKey.Mutanity,
    title: String = "",
    value: Double = 0.0,
) = Ratio(
    key = key,
    title = title,
    value = value,
)
