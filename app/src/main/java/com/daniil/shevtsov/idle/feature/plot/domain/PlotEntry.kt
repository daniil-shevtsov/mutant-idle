package com.daniil.shevtsov.idle.feature.plot.domain

data class PlotEntry(
    val text: String,
)

fun plotEntry(
    text: String,
) = PlotEntry(
    text = text,
)
