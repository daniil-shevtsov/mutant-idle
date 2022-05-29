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
import com.daniil.shevtsov.idle.feature.portrait.view.shrink
import com.daniil.shevtsov.idle.feature.portrait.view.toRect
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
    val pupilSize = iris.toRect().shrink(
        percent = 0.7f
    ).size
    val pupil = BodyPart(
        position = iris.toRect().center.translate(
            x = -pupilSize.width / 2,
            y = -pupilSize.height / 2,
        ),
        size = pupilSize,
        color = Color.Black
    )

    val pupilLightSize = pupil.toRect().shrink(percent = 0.3f).size
    val pupilLight = BodyPart(
        position = pupil.toRect().center.translate(
            x = -pupilLightSize.width / 2f,
            y = -pupilLightSize.height / 2f,
        ),
        size = pupilLightSize,
        color = Color.White
    )

    drawEyePart(eye)
    drawEyePart(iris)
    drawEyePart(pupil)
    drawEyePart(pupilLight)
}

fun DrawScope.drawEyePart(part: BodyPart) {
    drawOval(
        part.color,
        topLeft = part.position,
        size = part.size,
    )
}
