package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset

fun centeredSupportPoints(
    startPoint: Offset,
    endPoint: Offset,
): SupportPoints {
    val center = Offset(
        x = startPoint.x + (endPoint.x - startPoint.x) / 2,
        y = startPoint.y + (endPoint.y - startPoint.y) / 2,
    )
    return SupportPoints(
        startPoint = Offset(center.x, startPoint.y),
        endPoint = Offset(center.x, endPoint.y),
    )
}

fun bubblySupportPoints(
    startPoint: Offset,
    endPoint: Offset,
): SupportPoints {
    val distance = endPoint - startPoint

    return SupportPoints(
        startPoint = Offset(
            x = -distance.x,
            y = startPoint.y,
        ),
        endPoint = Offset(
            x = endPoint.x,
            y = distance.y,
        ),
    )
}
