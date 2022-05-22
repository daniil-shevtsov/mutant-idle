package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

fun standardBezierPoints(
    size: Size,
    supportPointsGenerator: SupportPointsGenerator = ::centeredSupportPoints,
): List<BezierPoint> {
    return bezierGenerator(size, supportPointsGenerator = supportPointsGenerator)
}

fun bezierGenerator(
    size: Size,
    numberOfSegments: Int = 10,
    deltaPercentage: (index: Int) -> Float = ::defaultDeltaPercentage,
    deltaSize: (height: Float, percent: Float) -> Float = ::delta,
    offsetSize: (height: Float, percent: Float) -> Float = ::topOffset,
    supportPointsGenerator: SupportPointsGenerator = ::centeredSupportPoints,
): List<BezierPoint> {
    val segmentXs = createSegments(size.width, numberOfSegments)
    val segments = createOffsets(segmentXs, offsetSize(size.height, 0.5f))

    val scatteredPoints = applyDeltas(
        segments = segments,
        deltaSign = ::isEven,
        deltaSize = { index ->
            val delta = deltaSize(size.height, 0.5f)
            -delta * 0.5f + delta * deltaPercentage(index)
        },
    )
    return generateBezier(scatteredPoints, supportPointsGenerator)
}

fun WavyShape(
    bezierPointsGenerator: (size: Size) -> List<BezierPoint>
) = GenericShape { size, _ ->
    drawBezierOutlinePath(
        size,
        bezierPointsGenerator(size)
    )
}

private fun Path.drawBezierOutlinePath(
    size: Size,
    bezierPoints: List<BezierPoint>,
) {
    val topLeft = Offset(0f, 0f)
    val bottomRight = Offset(size.width, size.height)
    val bottomLeft = Offset(0f, size.height)

    bezierPoints.forEachIndexed { index, bezierPoint ->
        with(bezierPoint) {
            if (index == 0) {
                reset()
                moveTo(startPoint.x, startPoint.y)
            }
            cubicTo(
                startSupportPoint.x,
                startSupportPoint.y,
                endSupportPoint.x,
                endSupportPoint.y,
                endPoint.x,
                endPoint.y
            )
            if (index == bezierPoints.size - 1) {
                lineTo(bottomRight.x, bottomRight.y)
                lineTo(bottomLeft.x, bottomLeft.y)
                lineTo(topLeft.x, topLeft.y)
            }
        }
    }
    close()
}

fun topOffset(height: Float, percent: Float = 0.5f) = height * percent
fun delta(height: Float, percent: Float = 0.2f) = height * percent

@Composable
@Preview
fun Shape() {
    val numberOfSegments = 10
    val randomFloats = IntRange(0, numberOfSegments).map { Random.nextFloat() }

    fun randomBezierGenerator(
        size: Size,
        supportPointsGenerator: SupportPointsGenerator = ::bubblySupportPoints,
    ) = bezierGenerator(
        size = size,
        numberOfSegments = 5,
        deltaPercentage = { index -> randomFloats[index] },
        supportPointsGenerator = supportPointsGenerator,
    )

    val shape = WavyShape(::randomBezierGenerator)

    Box(
        modifier = Modifier
            .width(200.dp)
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Red)
                .clip(shape)
                .background(Color.Black)
        )
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                fun addOvalAt(
                    point: Offset,
                    radius: Float = 10f,
                    color: Color = Color.White
                ) {
                    drawOval(
                        color,
                        point.minus(Offset(radius, radius)),
                        Size(radius * 2, radius * 2)
                    )
                }

                val topLeft = Offset(0f, 0f)
                val topRight = Offset(size.width, 0f)
                val bottomRight = Offset(size.width, size.height)
                val bottomLeft = Offset(0f, size.height)
                val center = topLeft.plus(Offset(size.width / 2, size.height / 2))

                addOvalAt(topLeft, color = Color.Black)
                addOvalAt(topRight, color = Color.Blue)
                addOvalAt(bottomRight, color = Color.Green)
                addOvalAt(bottomLeft, color = Color.Gray)
                addOvalAt(center)

                randomBezierGenerator(size).forEach { bezierPoint ->
                    with(bezierPoint) {
                        addOvalAt(startPoint, radius = 5f, color = Color.Gray)
                        addOvalAt(endPoint, radius = 5f, color = Color.Gray)
                        addOvalAt(startSupportPoint, radius = 5f, color = Color.White)
                        addOvalAt(endSupportPoint, radius = 5f, color = Color.White)
                    }
                }
            }
        )
    }

}
