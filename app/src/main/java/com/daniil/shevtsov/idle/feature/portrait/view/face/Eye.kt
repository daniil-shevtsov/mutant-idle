package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.*

data class EyeConfig(
    val iris: Float,
    val pupil: Float,
    val squint: Float,
)

@Preview
@Composable
fun EyePreview() {
    val config = EyeConfig(
        iris = 0.3f,
        pupil = 0.7f,
        squint = 0.2f
    )
    Canvas(modifier = Modifier.size(400.dp), onDraw = {
        drawEye(
            eyeArea = Rect(
                offset = Offset(0f, 0f),
                size = size
            ),
            config = config,
        )
    })
}

fun DrawScope.drawEye(
    eyeArea: Rect,
    config: EyeConfig,
    shouldShowAreas: Boolean = true,
) {
    val eyeLidSize = Size(
        width = eyeArea.width,
        height = (eyeArea.height / 2f) * config.squint,
    )
    val topEyeLidArea = Rect(
        offset = eyeArea.topLeft,
        size = eyeLidSize
    )
    val bottomEyeLidArea = Rect(
        offset = eyeArea.bottomLeft.translate(y = -eyeLidSize.height),
        size = eyeLidSize
    )

    val center = eyeArea.center
    val eyeSize = eyeArea.size
    val eye = BodyPart(
        position = center.translate(
            x = -eyeSize.width / 2f,
            y = -eyeSize.height / 2f,
        ),
        size = eyeSize,
        color = Color.White
    )

    val irisSize = Size(
        (eye.size.height + eye.size.width) / 2f,
        (eye.size.height + eye.size.width) / 2f
    ) * config.iris
    val iris = BodyPart(
        position = eye.position.translate(
            x = eye.size.width / 2f - irisSize.width / 2f,
            y = eye.size.height / 2f - irisSize.height / 2f,
        ),
        size = irisSize,
        color = Color.Green,
    )
    val pupilSize = iris.toRect()
        .shrink(
            percent = config.pupil
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

    val eyelidHackSize = Size(
        width = eye.size.width,
        height = eye.size.height - eyeLidSize.height * 2
    )
    val eyeLid = BodyPart(
        position = eye.toRect().centerLeft.translate(y = -eyelidHackSize.height / 2f),
        size = eyelidHackSize,
        color = Color.Blue,
    )
    val circlePath = Path().apply {
        addOval(Rect(eyeLid.position, eyeLid.size))
    }
    clipPath(circlePath, clipOp = ClipOp.Intersect) {
        drawEyePart(eye)
        drawEyePart(iris)
        drawEyePart(pupil)
        drawEyePart(pupilLight)
    }

    if (shouldShowAreas) {
        drawArea(eyeArea)
        drawArea(topEyeLidArea)
        drawArea(bottomEyeLidArea)
    }
}

fun DrawScope.drawEyePart(part: BodyPart) {
    drawOval(
        part.color,
        topLeft = part.position,
        size = part.size,
    )
}
