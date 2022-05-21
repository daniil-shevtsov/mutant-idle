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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
@Preview
fun Shape() {
    val shape = GenericShape { size, _ ->


        val h = size.height
        val halfH = h / 2f
        val w = size.width
        val delta = size.width / 3f

        val topLeft = Offset(0f, 0f)
        val topRight = Offset(size.width, 0f)
        val bottomRight = Offset(size.width, size.height)
        val bottomLeft = Offset(0f, size.height)
        val center = topLeft.plus(Offset(size.width, size.height))

        reset()
        moveTo(0f, h)
        cubicTo(0f, halfH, 0f, halfH, delta, halfH)
        lineTo(w - delta, halfH)
        cubicTo(w, halfH, w, halfH, w, 0f)
        lineTo(w, h)
        close()
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
        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            fun addOvalAt(point: Offset, radius: Float = 10f) {
                drawOval(
                    Color.White,
                    point.minus(Offset(radius, radius)),
                    Size(radius * 2, radius * 2)
                )
            }


            val topLeft = Offset(0f, 0f)
            val topRight = Offset(size.width, 0f)
            val bottomRight = Offset(size.width, size.height)
            val bottomLeft = Offset(0f, size.height)
            val center = topLeft.plus(Offset(size.width / 2, size.height / 2))

            addOvalAt(topLeft)
            addOvalAt(topRight)
            addOvalAt(bottomRight)
            addOvalAt(bottomLeft)
            addOvalAt(center)
        })
    }

}
