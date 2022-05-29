package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.translate

@Preview
@Composable
fun EyePreview() {
    Canvas(modifier = Modifier.size(400.dp), onDraw = {
        drawEye()
    })
}

fun DrawScope.drawEye() {
    val center = center
    val eyeSize = Size(
        width = size.width / 2f,
        height = size.width / 4f,
    )
    drawOval(
        Color.White,
        topLeft = center.translate(
            x = -eyeSize.width / 2f,
            y = -eyeSize.height / 2f,
        ),
        size = eyeSize,
    )
}
