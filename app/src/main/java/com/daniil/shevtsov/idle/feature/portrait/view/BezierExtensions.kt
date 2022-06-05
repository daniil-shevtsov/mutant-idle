package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.ui.geometry.Offset

data class BezierState(
    val start: Offset,
    val support: Offset,
    val support2: Offset? = null,
    val finish: Offset,
)

fun BezierState.points() = listOfNotNull(
    start,
    finish,
    support,
    support2,
)

fun List<Offset>.toBezierState() = BezierState(
    start = get(0),
    finish = get(1),
    support = get(2),
    support2 = getOrNull(3),
)
