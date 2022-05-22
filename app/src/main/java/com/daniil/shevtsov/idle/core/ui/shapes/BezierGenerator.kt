package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>,
    supportPointsGenerator: (startPoint: Offset, endPoint: Offset) -> SupportPoints = ::centeredSupportPoints,
): List<BezierPoint> {
    return points.zipWithNext().map { (startPoint, endPoint) ->
        val supportPoints = supportPointsGenerator(startPoint, endPoint)
        BezierPoint(
            startPoint = startPoint,
            endPoint = endPoint,
            startSupportPoint = supportPoints.startPoint,
            endSupportPoint = supportPoints.endPoint,
        )
    }
}
