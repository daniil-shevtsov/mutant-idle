package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.*
import kotlin.random.Random

@Preview(
    widthDp = 800,
    heightDp = 800
)
@Composable
fun PortraitPreview() {
    val previewSize = 400.dp

    val previewMode = PreviewMode.Portaits

    val state = PreviewState(
        mode = previewMode,
        shouldShowFaceAreas = true,
        shouldShowNoseAreas = false,
        shouldShowEyeAreas = false,
    )
    Column {
        repeat(2) {
            Row {
                repeat(2) {
                    Portrait(
                        previewState = state,
                        modifier = androidx.compose.ui.Modifier.size(previewSize)
                    )
                }
            }
        }
    }
}

fun DrawScope.drawPortrait(
    previewState: PreviewState
) {
    val screenArea = Rect(Offset(0f, 0f), Offset(size.width, size.height))

    val headArea = screenArea
        .shrink(0.8f)


    val headSize = Size(
        width = Random.nextFloatInRange(
            min = headArea.width * 0.4f,
            max = headArea.width
        ),
        height = Random.nextFloatInRange(
            min = headArea.height * 0.4f,
            max = headArea.height
        ),
    )
    val head = BodyPart(
        position = headArea.center.translate(
            x = -headSize.width / 2,
            y = -headSize.height / 2,
        ),
        size = headSize,
        color = Color.Gray
    )

    val faceArea = Rect(
        head.position,
        head.size,
    )

    val eyesArea = faceArea
        .shrink(
            widthPercent = 0.75f,
            heightPercent = 0.3f,
        )
        .move(faceArea.center.translate(y = -faceArea.height * (1 / 8f)))
    val eyeArea = eyesArea
        .shrink(widthPercent = 0.5f, heightPercent = 1f)
        .move(eyesArea.center.translate(x = -eyesArea.width * 0.25f))


    val noseArea = faceArea
        .shrink(
            widthPercent = 0.35f,
            heightPercent = 0.4f
        )
        .move(position = faceArea.center.translate(y = faceArea.height * (1 / 8f)))
    val mouthArea = faceArea
        .shrink(
            widthPercent = 0.75f,
            heightPercent = 0.2f
        )
        .move(faceArea.bottomCenter.translate(y = -faceArea.height * (1 / 8f)))


    val axisSize = 5f
    val verticalAxis = Rect(
        faceArea.topCenter.copy(x = faceArea.topCenter.x - axisSize),
        faceArea.bottomCenter.copy(x = faceArea.bottomCenter.x + axisSize),
    )

    val generatedSizes = FacePartsSize(
        eye = Size(
            width = Random.nextFloatInRange(
                min = eyeArea.width * 0.4f,
                max = eyeArea.width * 0.9f
            ),
            height = Random.nextFloatInRange(
                min = eyeArea.height * 0.3f,
                max = eyeArea.height * 0.6f
            ),
        ),
        nose = Size(
            width = Random.nextFloatInRange(
                min = noseArea.width * 0.5f,
                max = noseArea.width
            ),
            height = Random.nextFloatInRange(
                min = noseArea.height * 0.5f,
                max = noseArea.height
            ),
        ),
        mouth = Size(
            width = Random.nextFloatInRange(
                min = mouthArea.width * 0.3f,
                max = mouthArea.width
            ),
            height = Random.nextFloatInRange(
                min = mouthArea.height * 0.1f,
                max = mouthArea.height * 0.6f
            )
        ),
    )

    val eyeY = Random.nextFloatInRange(max = eyeArea.height - generatedSizes.eye.height)
    val eyeDistance =
        Random.nextFloatInRange(max = (eyesArea.width - generatedSizes.eye.width * 2) + 5f)
    val portraitState = PortraitState(
        head = head,
        leftEye = BodyPart(
            position = eyesArea.topCenter.translate(
                x = (-eyeDistance / 2) - generatedSizes.eye.width,
                y = eyeY
            ),
            size = generatedSizes.eye,
            color = Color.Green
        ),
        rightEye = BodyPart(
            position = eyesArea.topCenter.translate(
                x = eyeDistance / 2,
                y = eyeY
            ),
            size = generatedSizes.eye,
            color = Color.Green
        ),
        nose = BodyPart(
            position = noseArea.topCenter.translate(
                x = -generatedSizes.nose.width / 2,
                y = Random.nextFloatInRange(max = noseArea.height - generatedSizes.nose.height)
            ),
            size = generatedSizes.nose,
            color = Color.LightGray
        ),
        mouth = BodyPart(
            position = verticalAxis.bottomCenter.translate(
                x = -generatedSizes.mouth.width / 2,
                y = -verticalAxis.size.height * 0.10f - generatedSizes.mouth.height / 2,
            ),
            size = generatedSizes.mouth,
            color = Color.White
        ),
    )

    drawRect(Color.DarkGray, topLeft = screenArea.topLeft, size = screenArea.size)
    drawBodyPart(portraitState.head)

    val eyeConfig = EyeConfig(
        iris = Random.nextFloatInRange(
            min = 0.4f,
            max = 0.7f,
        ),
        pupil = Random.nextFloatInRange(
            min = 0.6f,
            max = 0.8f,
        ),
        squint = Random.nextFloatInRange(
            min = 0.2f,
            max = 0.6f,
        ),
    )

    drawHead(
        headArea = faceArea,
        state = HeadViewState(
            topArea = BezierState(
                start = Offset(0f, 0.5f),
                finish = Offset(1f, 0.5f),
                support = Offset(0f, 0f),
                support2 = Offset(1f, 0f),
            ),
            bottomArea = BezierState(
                start = Offset(0f, 0.5f),
                finish = Offset(1f, 0.5f),
                support = Offset(0f, 1f),
                support2 = Offset(1f, 1f),
            ),
            previousSelectedIndex = -1,
        ),
        onStateChanged = {},
    )

    drawEye(
        eyeArea = portraitState.leftEye.toRect(),
        config = eyeConfig,
        shouldShowAreas = previewState.shouldShowEyeAreas
    )
    drawEye(
        eyeArea = portraitState.rightEye.toRect(),
        config = eyeConfig,
        shouldShowAreas = previewState.shouldShowEyeAreas
    )
    drawBodyPart(portraitState.mouth)

    drawNose(portraitState.nose, previewState = previewState)

    if (previewState.shouldShowFaceAreas) {
        drawArea(headArea)
        drawArea(faceArea)
        drawArea(eyesArea)
        drawArea(eyeArea)
        drawArea(noseArea)
        drawArea(mouthArea)
    }
}
