package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>
): List<BezierPoint> {
    return points.zipWithNext().map { (startPoint, secondPoint) ->
        val center = Offset(
            x = startPoint.x + (secondPoint.x - startPoint.x) / 2,
            y = startPoint.y + (secondPoint.y - startPoint.y) / 2,
        )
        BezierPoint(
            startPoint = startPoint,
            endPoint = secondPoint,
            startSupportPoint = center,
            endSupportPoint = center,
        )
    }
}
