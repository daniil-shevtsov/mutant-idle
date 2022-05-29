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
import com.daniil.shevtsov.idle.feature.portrait.view.BodyPart
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
    val eye = BodyPart(
        position = center.translate(
            x = -eyeSize.width / 2f,
            y = -eyeSize.height / 2f,
        ),
        size = eyeSize,
        color = Color.White
    )

    val irisSize = Size(
        eye.size.height,
        eye.size.height
    )
    val iris = BodyPart(
        position = eye.position.translate(
            x = eye.size.width / 2f - irisSize.width / 2f,
            y = eye.size.height / 2f - irisSize.height / 2f,
        ),
        size = irisSize,
        color = Color.Green,
    )

    drawOval(
        eye.color,
        topLeft = eye.position,
        size = eye.size,
    )
    drawOval(
        iris.color,
        topLeft = iris.position,
        size = iris.size
    )
}
