package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>
): List<BezierPoint> {
    return points.zipWithNext().map { (startPoint, secondPoint) ->
        val center = startPoint.x + (secondPoint.x - startPoint.x) / 2
        BezierPoint(
            startPoint = startPoint,
            endPoint = secondPoint,
            startSupportPoint = Offset(center, startPoint.y),
            endSupportPoint = Offset(center, secondPoint.y),
        )
    }
}
