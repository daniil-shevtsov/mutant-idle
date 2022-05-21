package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>
): List<BezierPoint> {
    return points.zipWithNext().map { (startPoint, endPoint) ->
        val center = Offset(
            x = startPoint.x + (endPoint.x - startPoint.x) / 2,
            y = startPoint.y + (endPoint.y - startPoint.y) / 2,
        )
        BezierPoint(
            startPoint = startPoint,
            endPoint = endPoint,
            startSupportPoint = Offset(center.x, startPoint.y),
            endSupportPoint = Offset(center.x, endPoint.y),
        )
    }
}
