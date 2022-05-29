package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

data class AreaPercentages(
    val faceArea: Size,
    val eyesArea: Size,
    val noseArea: Size,
    val mouthArea: Size,
)

data class GeneratingConfig(
    val faceArea: Rect,
    val eyesArea: Rect,
    val noseArea: Rect,
    val mouthArea: Rect,
)

data class BodyPart(
    val position: Offset,
    val size: Size,
    val color: Color,
)

data class FacePartsSize(
    val eye: Size,
    val nose: Size,
    val mouth: Size,
)

data class PortraitState(
    val head: BodyPart,
    val leftEye: BodyPart,
    val rightEye: BodyPart,
    val nose: BodyPart,
    val mouth: BodyPart,
)

data class BezierState(
    val start: Offset,
    val support: Offset,
    val finish: Offset,
)

data class NoseState(
    val nostril: BezierState,
    val tip: BezierState
)

enum class PreviewMode {
    Portaits,
    Nose,
}

data class PreviewState(
    val mode: PreviewMode,
    val shouldShowFaceAreas: Boolean,
    val shouldShowNoseAreas: Boolean,
)

@Preview(
    widthDp = 800,
    heightDp = 800
)
@Composable
fun PortraitPreview() {
    val previewSize = 400.dp

    val previewMode = PreviewMode.Nose

    val state = PreviewState(
        mode = previewMode,
        shouldShowFaceAreas = false,
        shouldShowNoseAreas = true,
    )

    when (state.mode) {
        PreviewMode.Nose -> {
            Canvas(modifier = Modifier, onDraw = {
                val nose = BodyPart(
                    position = Offset(size.width / 2 - size.width / 4, 0f),
                    size = Size(size.width / 2, size.height),
                    color = Color.Gray
                )
                drawNose(nose, state)
            })
        }
        PreviewMode.Portaits -> {
            Column {
                repeat(2) {
                    Row {
                        repeat(2) {
                            Portrait(
                                previewState = state,
                                modifier = Modifier.size(previewSize)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun DrawScope.drawBezierPoints(
    bezierState: BezierState,
    pointColor: Color = Color.Green,
    supportColor: Color = Color.Red,
    pointRadius: Float = 16f,
) {
    drawCircle(pointColor, center = bezierState.start, radius = pointRadius)
    drawCircle(pointColor, center = bezierState.finish, radius = pointRadius)
    drawCircle(supportColor, center = bezierState.support, radius = pointRadius)
}

@Composable
fun Portrait(
    previewState: PreviewState,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier, onDraw = {
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
        val axisColor = Color.Blue

        val horizontalAxis = Rect(
            faceArea.centerLeft.copy(y = faceArea.centerLeft.y - axisSize),
            faceArea.centerRight.copy(y = faceArea.centerLeft.y + axisSize),
        )
        val verticalAxis = Rect(
            faceArea.topCenter.copy(x = faceArea.topCenter.x - axisSize),
            faceArea.bottomCenter.copy(x = faceArea.bottomCenter.x + axisSize),
        )

        val generatingConfig = GeneratingConfig(
            faceArea = faceArea,
            eyesArea = eyesArea,
            noseArea = noseArea,
            mouthArea = mouthArea,
        )

        val generatedSizes = FacePartsSize(
            eye = Size(
                width = Random.nextFloatInRange(
                    min = eyeArea.width * 0.2f,
                    max = eyeArea.width * 0.9f
                ),
                height = Random.nextFloatInRange(
                    min = eyeArea.height * 0.1f,
                    max = eyeArea.height * 0.6f
                ),
            ),
            nose = Size(
                width = Random.nextFloatInRange(
                    min = noseArea.width * 0.1f,
                    max = noseArea.width
                ),
                height = Random.nextFloatInRange(
                    min = noseArea.height * 0.3f,
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
            Random.nextFloatInRange(max = eyesArea.width - generatedSizes.eye.width * 2)
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
        drawRect(axisColor, topLeft = horizontalAxis.topLeft, size = horizontalAxis.size)
        drawRect(axisColor, topLeft = verticalAxis.topLeft, size = verticalAxis.size)

        drawBodyPart(portraitState.leftEye)
        drawBodyPart(portraitState.rightEye)
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
    })
}

fun DrawScope.drawNose(
    nose: BodyPart,
    previewState: PreviewState,
) {
    val noseArea = nose.toRect()
    val dorsumArea = noseArea
        .shrink(
            widthPercent = Random.nextFloatInRange(
                min = 0.3f,
                max = 0.8f,
            )
        )
    val nostrilsArea = noseArea
        .shrink(heightPercent = 0.25f)
        .let {
            it.move(
                nose.position.translate(
                    x = it.size.width / 2,
                    y = nose.size.height - it.size.height
                )
            )
        }
    val tipArea = Rect(
        dorsumArea.bottomLeft.translate(
            y = -(dorsumArea.bottomLeft.y - nostrilsArea.bottomLeft.y)
        ),
        Size(
            width = dorsumArea.width,
            height = dorsumArea.bottomLeft.y - nostrilsArea.bottomLeft.y
        )
    )
    val dorsumSize = Size(
        dorsumArea.width,
        dorsumArea.height - tipArea.height
    )
    val dorsum = BodyPart(
        position = dorsumArea.topCenter.translate(
            x = -dorsumSize.width / 2f
        ),
        size = dorsumSize,
        color = Color.LightGray
    )

    val leftNostrilArea = Rect(
        nostrilsArea.topLeft,
        Size(
            width = (dorsumArea.left - nostrilsArea.left),
            height = nostrilsArea.height,
        )
    )
    val rightNostrilArea = Rect(
        nostrilsArea.topRight.translate(
            x = -(nostrilsArea.right - dorsumArea.right)
        ),
        Size(
            width = nostrilsArea.right - dorsumArea.right,
            height = nostrilsArea.height,
        )
    )
    val nostrilsSupportY = Random.nextFloatInRange(
        min = 0f,
        max = nostrilsArea.height
    )
//    val nostrilWidth = (nostrilsArea.width - dorsumArea.width)  / 2f
    val nostrilWidth = Random.nextFloatInRange(
        min = (nostrilsArea.width - dorsumArea.width) / 8f,
        max = (nostrilsArea.width - dorsumArea.width) / 2f,
    )

    val leftNostril = BezierState(
        start = leftNostrilArea.topRight,
        finish = leftNostrilArea.bottomRight,
        support = leftNostrilArea.topRight
            .translate(
                x = -nostrilWidth * 2,
                y = nostrilsSupportY,
            ),
    )

    val rightNostril = BezierState(
        start = rightNostrilArea.topLeft,
        finish = rightNostrilArea.bottomLeft,
        support = rightNostrilArea.topRight
            .translate(
                x = rightNostrilArea.width,
                y = nostrilsSupportY
            ),
    )

    val noseTip = BezierState(
        start = tipArea.topLeft,
        finish = tipArea.topRight,
        support = tipArea.bottomCenter.translate(y = tipArea.height)
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(rightNostril)
            close()
        },
        Color.LightGray,
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(leftNostril)
            close()
        },
        Color.LightGray,
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(noseTip)
            close()
        },
        Color.LightGray,
    )
    drawBodyPart(dorsum)
    drawBezierPoints(leftNostril)
    drawBezierPoints(rightNostril)

    if (previewState.shouldShowNoseAreas) {
        drawArea(noseArea)
        drawArea(dorsumArea)
        drawArea(nostrilsArea)
        drawArea(leftNostrilArea)
        drawArea(rightNostrilArea)
        drawArea(tipArea)
    }
}

fun BodyPart.toRect() = Rect(
    position,
    size,
)

fun DrawScope.drawBodyPart(part: BodyPart) {
    drawRect(part.color, topLeft = part.position, size = part.size)
}

fun Path.drawQuadraticBezier(state: BezierState) {
    with(state) {
        moveTo(start.x, start.y)
        quadraticBezierTo(
            support.x,
            support.y,
            finish.x,
            finish.y,
        )
    }
}

fun DrawScope.drawArea(
    area: Rect,
    color: Color = Color.Red,
    strokeWidth: Float = 3f,
) {
    drawRect(
        color,
        style = Stroke(width = strokeWidth),
        topLeft = area.topLeft,
        size = area.size
    )
}

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

fun Random.nextFloatInRange(
    min: Float = Float.MIN_VALUE,
    max: Float = Float.MAX_VALUE,
) = min + nextFloat() * (max - min)
