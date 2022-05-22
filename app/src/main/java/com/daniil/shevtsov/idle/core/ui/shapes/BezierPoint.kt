package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

data class BezierPoint(
    val startPoint: Offset,
    val endPoint: Offset,
    val startSupportPoint: Offset,
    val endSupportPoint: Offset,
)
