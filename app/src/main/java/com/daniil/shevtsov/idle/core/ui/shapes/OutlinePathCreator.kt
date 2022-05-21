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
): List<Float> = IntRange(0, numberOfSegments).map { segment ->
    (length / numberOfSegments) * segment
}

fun createOffsets(
    segmentXs: List<Float>,
    y: Float,
): List<Offset> = segmentXs.map { segmentX ->
    Offset(segmentX, y)
}

fun applyDeltas(
    segments: List<Offset>,
    deltaSign: (index: Int) -> Boolean,
    deltaSize: (index: Int) -> Float,
): List<Offset> {
    return segments.mapIndexed { index, segment ->
        val sign = when (deltaSign(index)) {
            true -> -1
            false -> 1
        }
        segment.copy(
            x = segment.x,
            y = segment.y + sign * deltaSize(index)
        )
    }
}

fun isEven(index: Int): Boolean = when (index % 2) {
    0 -> true
    else -> false
}

//fun createDeltas(
//    segments: List<Float>,
//    deltaGenerator: (segment: Float) -> Offset
//): List<DeltaSegment> {
//    return segments.map { segment ->
//        DeltaSegment(
//            segment = segment,
//            delta = deltaGenerator(segment),
//        )
//    }
//}


fun scatterSegments(
    segments: List<Float>,
): List<Offset> {
    return emptyList()
}

data class DeltaSegment(
    val segment: Offset,
    val delta: Float,
)

data class OutlinePath(
    val bounds: Rect,
    val points: List<Offset>,
)
