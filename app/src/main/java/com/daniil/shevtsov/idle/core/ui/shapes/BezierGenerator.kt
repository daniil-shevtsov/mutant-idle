package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>
): List<BezierPoint> {
    return if (points.size >= 2) {
        val center = (points[1].x - points[0].x) / 2
        val firstBezierPoint = BezierPoint(
            startPoint = points[0],
            endPoint = points[1],
            startSupportPoint = Offset(center, points[0].y),
            endSupportPoint = Offset(center, points[1].y),
        )
        return listOf(firstBezierPoint)
    } else {
        emptyList()
    }

}
