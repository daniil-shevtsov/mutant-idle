package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun generateBezier(
    points: List<Offset>
): List<BezierPoint> {
    return when (points.size) {
        2 -> {
            val center = (points[1].x - points[0].x) / 2
            val firstBezierPoint = BezierPoint(
                startPoint = points[0],
                endPoint = points[1],
                startSupportPoint = Offset(center, points[0].y),
                endSupportPoint = Offset(center, points[1].y),
            )
            listOf(firstBezierPoint)
        }
        3 -> {
            val firstCenter = points[0].x + (points[1].x - points[0].x) / 2
            val firstBezierPoint = BezierPoint(
                startPoint = points[0],
                endPoint = points[1],
                startSupportPoint = Offset(firstCenter, points[0].y),
                endSupportPoint = Offset(firstCenter, points[1].y),
            )
            val secondCenter = points[1].x + (points[2].x - points[1].x) / 2
            val secondBezierPoint = BezierPoint(
                startPoint = points[1],
                endPoint = points[2],
                startSupportPoint = Offset(secondCenter, points[1].y),
                endSupportPoint = Offset(secondCenter, points[2].y),
            )
            listOf(firstBezierPoint, secondBezierPoint)
        }
        else -> {
            emptyList()
        }
    }

}
