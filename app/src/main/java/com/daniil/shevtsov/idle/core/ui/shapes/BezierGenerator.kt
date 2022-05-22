package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

typealias SupportPointsGenerator = (startPoint: Offset, endPoint: Offset) -> SupportPoints

fun generateBezier(
    points: List<Offset>,
    supportPointsGenerator: SupportPointsGenerator = ::centeredSupportPoints,
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
