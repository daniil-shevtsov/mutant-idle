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

fun ratioChange(
    key: RatioKey = RatioKey.Mutanity,
    change: Double = 0.0,
) = key to change
