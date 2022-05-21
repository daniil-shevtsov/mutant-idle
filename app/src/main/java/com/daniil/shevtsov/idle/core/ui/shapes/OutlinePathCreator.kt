package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

fun createOutlinePath(
    bounds: Rect
): List<Offset> {
    return listOf(
        Offset(bounds.left, bounds.top),
        Offset(bounds.right, bounds.top),
        Offset(bounds.right, bounds.bottom),
        Offset(bounds.left, bounds.bottom),
        Offset(bounds.left, bounds.top),
    )
}

fun createSegments(
    length: Float,
    numberOfSegments: Int,
): List<Float> =
    IntRange(0, numberOfSegments).map { segment -> (length / numberOfSegments) * segment }

fun createDeltas(
    segments: List<Float>,
    deltaGenerator: (segment: Float) -> Offset
): List<DeltaSegment> {
    return segments.map { segment ->
        DeltaSegment(
            segment = segment,
            delta = deltaGenerator(segment),
        )
    }
}

fun oddEvenDeltaGenerator(segment: Float): Offset {
    return deltaGenerator(
        segment = segment,
        deltaSize = Offset(
            x = segment,
            y = when (segment % 2) {
                0f -> -10f
                else -> 10f
            }
        ),
    )
}

fun deltaGenerator(segment: Float, deltaSize: Offset): Offset {
    return deltaSize
}

data class DeltaSegment(
    val segment: Float,
    val delta: Offset,
)
