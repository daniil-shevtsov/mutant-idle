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

private fun createSegments(
    size: Size,
    numberOfSegments: Int = 10,
    randomFloats: List<Float>,
    delta: Float = size.height * 0.1f,
) = randomFloats.mapIndexed { segment, randomFloat ->
    Offset(
        x = (size.width / numberOfSegments) * segment,
        y = 30 - (delta - randomFloat * delta)
    )
}

private fun Path.drawMyPath(
    size: Size,
    segments: List<Offset>,
) {
    val topLeft = Offset(0f, 0f)
    val topRight = Offset(size.width, 0f)
    val bottomRight = Offset(size.width, size.height)
    val bottomLeft = Offset(0f, size.height)
    val center = topLeft.plus(Offset(size.width / 2, size.height / 2))

    reset()
    moveTo(topLeft.x, topLeft.y)
    lineTo(center.x, center.y)
    lineTo(topRight.x, topRight.y)
    close()
}

@Composable
@Preview
fun Shape() {
    val numberOfSegments = 10
    val randomFloats = IntRange(0, numberOfSegments).map { Random.nextFloat() }

    val shape = GenericShape { size, _ ->

        val topLeft = Offset(0f, 0f)
        val topRight = Offset(size.width, 0f)
        val bottomRight = Offset(size.width, size.height)
        val bottomLeft = Offset(0f, size.height)
        val center = topLeft.plus(Offset(size.width, size.height))

        drawMyPath(
            size,
            createSegments(size, numberOfSegments, randomFloats)
        )
    }
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

                val segments = createSegments(size, numberOfSegments, randomFloats)

                drawPath(Path().apply { drawMyPath(size, segments) }, Color.Cyan)

                addOvalAt(topLeft, color = Color.Black)
                addOvalAt(topRight, color = Color.Blue)
                addOvalAt(bottomRight, color = Color.Green)
                addOvalAt(bottomLeft, color = Color.Gray)
                addOvalAt(center)

                segments.forEach { segment ->
                    addOvalAt(segment, radius = 5f, color = Color.Yellow)
                }
            }
        )
    }

}
