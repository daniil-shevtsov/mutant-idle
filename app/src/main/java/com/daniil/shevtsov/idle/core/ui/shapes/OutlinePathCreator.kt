package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

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

fun defaultDeltaPercentage(index: Int): Float = 1f
