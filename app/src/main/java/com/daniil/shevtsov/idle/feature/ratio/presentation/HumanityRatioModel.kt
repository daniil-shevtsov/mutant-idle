package com.daniil.shevtsov.idle.feature.ratio.presentation

data class HumanityRatioModel(
    val title: String,
    val name: String,
    val percent: Double,
    val percentLabel: String,
)

fun humanityRatioModel(
    title: String = "",
    name: String = "",
    percent: Double = 0.0,
    percentLabel: String = "",
) = HumanityRatioModel(
    title = title,
    name = name,
    percent = percent,
    percentLabel = percentLabel,
)
