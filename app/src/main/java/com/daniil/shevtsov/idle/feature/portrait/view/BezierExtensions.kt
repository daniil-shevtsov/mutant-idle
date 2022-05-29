package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.ui.geometry.Offset

data class BezierState(
    val start: Offset,
    val support: Offset,
    val support2: Offset? = null,
    val finish: Offset,
)
