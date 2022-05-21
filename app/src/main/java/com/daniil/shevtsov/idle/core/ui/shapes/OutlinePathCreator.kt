package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect

fun createOutlinePath(
    bounds: Rect
): OutlinePath {
    return OutlinePath(
        bounds = bounds,
        points = listOf(
            Offset(bounds.left, bounds.top),
            Offset(bounds.right, bounds.top),
            Offset(bounds.right, bounds.bottom),
            Offset(bounds.left, bounds.bottom),
            Offset(bounds.left, bounds.top),
        )
    )
}

fun bezierOutlinePath(
    outlinePath: OutlinePath
): OutlinePath {
    return outlinePath.copy(
        points = bezierEffect(outlinePath)
    )
}

fun bezierEffect(outlinePath: OutlinePath): List<Offset> {
    return outlinePath.points.map { it }
}

fun createSegments(
    length: Float,
    numberOfSegments: Int,
): List<Float> =
    IntRange(0, numberOfSegments).map { segment ->
        (length / numberOfSegments) * segment
    }

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

fun scatterSegments(
    segments: List<Float>,
): List<Offset> {
    return emptyList()
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

fun randomDeltaGenerator(segment: Float, randomFloat: Float): Offset {
    return deltaGenerator(
        segment = segment,
        deltaSize = Offset(
            x = segment,
            y = randomFloat
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

data class OutlinePath(
    val bounds: Rect,
    val points: List<Offset>,
)
