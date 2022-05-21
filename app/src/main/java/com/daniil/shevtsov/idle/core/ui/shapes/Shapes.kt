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

data class BezierOutlineConfig(
    val size: Size,
    val numberOfSegments: Int = 10,
    val topOffset: Float = size.height * 0.5f,
    val delta: Float = size.height * 0.2f,
)

fun WavyShape(
    numberOfSegments: Int = 10,
    randomFloats: List<Float> = IntRange(0, numberOfSegments).map { Random.nextFloat() },
    topOffsetHeightPercent: Float = 0.5f,
    deltaHeightPercent: Float = 0.2f,
) = GenericShape { size, _ ->

    val topLeft = Offset(0f, 0f)
    val topRight = Offset(size.width, 0f)
    val bottomRight = Offset(size.width, size.height)
    val bottomLeft = Offset(0f, size.height)
    val center = topLeft.plus(Offset(size.width, size.height))

    val segmentXs = createSegments(size.width, numberOfSegments)

    val scatteredPoints = createDeltas(
        segments = segmentXs,
        deltaGenerator = ::oddEvenDeltaGenerator,
    ).map { it.delta }
    val bezierPoints = generateBezier(scatteredPoints)

    drawBezierOutlinePath(
        size,
        generateBezier(
            createSegments(
                size = size,
                numberOfSegments = numberOfSegments,
                randomFloats = randomFloats,
                topOffset = topOffsetHeightPercent * size.height,
                delta = deltaHeightPercent * size.height,
            )
        )
    )
}

private fun createSegments(
    size: Size,
    numberOfSegments: Int = 10,
    randomFloats: List<Float>,
    topOffset: Float,
    delta: Float,
) = randomFloats.mapIndexed { segment, randomFloat ->
    Offset(
        x = (size.width / numberOfSegments) * segment,
        y = topOffset - delta * 0.5f + delta * randomFloat
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

@Composable
@Preview
fun Shape() {
    val numberOfSegments = 10
    val randomFloats = IntRange(0, numberOfSegments).map { Random.nextFloat() }

    val shape = WavyShape(
        numberOfSegments = numberOfSegments,
        randomFloats = randomFloats,
    )
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

                val delta = size.height * 0.2f
                val topOffset = size.height * 0.5f
                val segmentXs = createSegments(size.width, numberOfSegments)
                val segments = createDeltas(
                    segments = segmentXs,
                    deltaGenerator = { segment ->
                        Offset(
                            segment,
                            topOffset - delta * 0.5f + delta * randomFloats[segmentXs.indexOf(
                                segment
                            )]
                        )
                    }
                ).map { it.delta }

                val bezierPoints = generateBezier(segments)
                drawPath(Path().apply { drawBezierOutlinePath(size, bezierPoints) }, Color.Cyan)

                addOvalAt(topLeft, color = Color.Black)
                addOvalAt(topRight, color = Color.Blue)
                addOvalAt(bottomRight, color = Color.Green)
                addOvalAt(bottomLeft, color = Color.Gray)
                addOvalAt(center)

                randomFloats.mapIndexed { segment, randomFloat ->
                    Offset(
                        x = topLeft.x + (size.width / numberOfSegments) * segment,
                        y = topLeft.y + topOffset * -delta * 0.5f + delta * randomFloat
                    )
                }

                segments.forEach { segment ->
                    addOvalAt(segment, radius = 5f, color = Color.Yellow)
                }
                bezierPoints.forEach { bezierPoint ->
                    with(bezierPoint) {
                        addOvalAt(startPoint, radius = 5f, color = Color.Black)
                        addOvalAt(endPoint, radius = 5f, color = Color.Black)
                        addOvalAt(startSupportPoint, radius = 5f, color = Color.White)
                        addOvalAt(endSupportPoint, radius = 5f, color = Color.White)
                    }
                }

//                val outlinePath = createOutlinePath(bounds = Rect(Offset(0f, 0f), size)).points
//                drawPath(Path().apply {
//                    outlinePath.forEachIndexed { index, outlinePoint ->
//                        when {
//                            index == 0 -> {
//                                reset()
//                                moveTo(outlinePoint.x, outlinePoint.y)
//                            }
//                            index < outlinePath.size - 1 -> {
//                                lineTo(outlinePoint.x, outlinePoint.y)
//                            }
//                            else -> {
//                                close()
//                            }
//                        }
//                    }
//                }, Color.Blue)
            }
        )
    }

}
