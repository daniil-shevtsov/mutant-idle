package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PortraitPreview() {
    val previewSize = 400.dp
    Column {
        Portrait(
            modifier = Modifier.size(previewSize)
        )
//        Portrait(
//            modifier = Modifier.size(50.dp)
//        )
//        Portrait(
//            modifier = Modifier.size(previewSize)
//        )
//        Portrait(
//            modifier = Modifier.size(previewSize)
//        )
//        Portrait(
//            modifier = Modifier.size(previewSize)
//        )
    }


}

@Composable
fun Portrait(
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier, onDraw = {
        val screenArea = Rect(Offset(0f, 0f), Offset(size.width, size.height))

        val faceArea = screenArea
            .shrink(0.8f)

        val eyeArea = faceArea
            .shrink(
                widthPercent = 0.75f,
                heightPercent = 0.3f,
            )
            .move(faceArea.center)

        val noseArea = Rect(
            faceArea.topLeft.translate(x = 300f, y = 400f),
            faceArea.bottomRight.translate(x = -300f, y = -150f),
        )
        val mouthArea = Rect(
            faceArea.topLeft.translate(x = 100f, y = 700f),
            faceArea.bottomRight.translate(x = -100f, y = -20f),
        )

        val axisSize = 5f
        val axisColor = Color.Blue
        val partColor = Color.Green
        val partAreaColor = Color.Red
        val strokeWidth = 3f
        val horizontalAxis = Rect(
            faceArea.centerLeft.copy(y = faceArea.centerLeft.y - axisSize),
            faceArea.centerRight.copy(y = faceArea.centerLeft.y + axisSize),
        )
        val verticalAxis = Rect(
            faceArea.topCenter.copy(x = faceArea.topCenter.x - axisSize),
            faceArea.bottomCenter.copy(x = faceArea.bottomCenter.x + axisSize),
        )

        val mouthSize = Size(400f, 50f)
        val mouth = Rect(
            offset = verticalAxis.bottomCenter.translate(
                x = -mouthSize.width / 2,
                y = -verticalAxis.size.height * 0.10f - mouthSize.height / 2,
            ),
            size = mouthSize,
        )

        val noseSize = Size(50f, 300f)
        val nose = Rect(
            offset = verticalAxis.center.translate(
                x = -noseSize.width / 2
            ),
            size = noseSize,
        )

        val eyeSize = Size(100f, 50f)
        val leftEye = Rect(
            offset = horizontalAxis.centerLeft.translate(
                x = horizontalAxis.width * 0.75f - eyeSize.width / 2,
                y = -eyeSize.height / 2
            ),
            size = eyeSize
        )
        val rightEye = Rect(
            offset = horizontalAxis.centerLeft.translate(
                x = horizontalAxis.width * 0.25f - eyeSize.width / 2,
                y = -eyeSize.height / 2
            ),
            size = eyeSize
        )

        drawRect(Color.DarkGray, topLeft = screenArea.topLeft, size = screenArea.size)
        drawRect(Color.Gray, topLeft = faceArea.topLeft, size = faceArea.size)

        drawRect(axisColor, topLeft = horizontalAxis.topLeft, size = horizontalAxis.size)
        drawRect(axisColor, topLeft = verticalAxis.topLeft, size = verticalAxis.size)

        drawRect(
            partAreaColor,
            style = Stroke(width = strokeWidth),
            topLeft = eyeArea.topLeft,
            size = eyeArea.size
        )
        drawRect(partColor, topLeft = leftEye.topLeft, size = leftEye.size)
        drawRect(partColor, topLeft = rightEye.topLeft, size = rightEye.size)

        drawRect(
            partAreaColor,
            style = Stroke(width = strokeWidth),
            topLeft = noseArea.topLeft,
            size = noseArea.size
        )
        drawRect(partColor, topLeft = nose.topLeft, size = nose.size)

        drawRect(
            partAreaColor,
            style = Stroke(width = strokeWidth),
            topLeft = mouthArea.topLeft,
            size = mouthArea.size
        )
        drawRect(partColor, topLeft = mouth.topLeft, size = mouth.size)
    })
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
    widthPercent: Float,
    heightPercent: Float,
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
