package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun Rect.shrink(percent: Float): Rect {
    return shrink(
        widthPercent = percent, heightPercent = percent
    )
}

enum class Anchor {
    Center
}

fun Rect.move(
    position: Offset,
    anchor: Anchor = Anchor.Center,
) = Rect(
    offset = position.translate(
        x = -width / 2,
        y = -height / 2,
    ),
    size = size,
)

fun Rect.shrink(
    widthPercent: Float = 1f,
    heightPercent: Float = 1f,
): Rect {
    val newWidth = width * widthPercent
    val newHeight = height * heightPercent

    return Rect(
        offset = topLeft.translate(
            x = (width - newWidth) / 2,
            y = (height - newHeight) / 2,
        ),
        size = Size(newWidth, newHeight)
    )
}

fun Offset.translate(
    value: Float,
) = translate(
    x = value,
    y = value,
)

fun Offset.translate(
    x: Float = 0f,
    y: Float = 0f,
) = copy(
    x = this.x + x,
    y = this.y + y,
)

fun Offset.distanceTo(offset: Offset) = sqrt((offset.x - x).pow(2) + (offset.y - y).pow(2))

fun Offset.coerceIn(bounds: Rect) = Offset(
    x = x.coerceIn(bounds.left, bounds.right),
    y = y.coerceIn(bounds.top, bounds.bottom)
)

fun Offset.times(
    x: Float = 1f,
    y: Float = 1f
) = Offset(
    x = this.x * x,
    y = this.y * y,
)

fun Offset.div(
    x: Float = 1f,
    y: Float = 1f
) = Offset(
    x = this.x / x,
    y = this.y / y,
)

fun Random.nextFloatInRange(
    min: Float = Float.MIN_VALUE,
    max: Float = Float.MAX_VALUE,
) = min + nextFloat() * (max - min)

fun BodyPart.toRect() = Rect(
    position,
    size,
)
