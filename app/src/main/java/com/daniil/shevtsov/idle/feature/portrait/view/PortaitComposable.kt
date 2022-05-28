package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PortraitPreview() {
    Portrait(modifier = Modifier.size(400.dp))
}

@Composable
fun Portrait(modifier: Modifier) {
    Canvas(modifier = modifier, onDraw = {
        val screenArea = Rect(Offset(0f, 0f), Offset(size.width, size.height))

        val faceArea = Rect(
            screenArea.topLeft.translate(100f),
            screenArea.bottomRight.translate(-100f),
        )

        val axisSize = 5f
        val axisColor = Color.Blue
        val partColor = Color.Green
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
            offset = Offset(
                verticalAxis.center.x - mouthSize.width / 2,
                verticalAxis.topCenter.y + verticalAxis.size.height * 0.85f
            ),
            size = mouthSize,
        )

        val noseSize = Size(50f, 300f)
        val nose = Rect(
            offset = Offset(
                verticalAxis.center.x - noseSize.width / 2,
                verticalAxis.center.y,
            ),
            size = noseSize,
        )

        val eyeSize = Size(100f, 50f)
        val leftEye = Rect(
            offset = Offset(
                horizontalAxis.centerLeft.x + horizontalAxis.width * 0.75f - eyeSize.width / 2,
                horizontalAxis.center.y - eyeSize.height / 2,
            ),
            size = eyeSize
        )
        val rightEye = Rect(
            offset = Offset(
                horizontalAxis.centerLeft.x + horizontalAxis.width * 0.25f - eyeSize.width / 2,
                horizontalAxis.center.y - eyeSize.height / 2,
            ),
            size = eyeSize
        )

        drawRect(Color.DarkGray, topLeft = screenArea.topLeft, size = screenArea.size)
        drawRect(Color.Gray, topLeft = faceArea.topLeft, size = faceArea.size)

        drawRect(axisColor, topLeft = horizontalAxis.topLeft, size = horizontalAxis.size)
        drawRect(axisColor, topLeft = verticalAxis.topLeft, size = verticalAxis.size)

        drawRect(partColor, topLeft = leftEye.topLeft, size = leftEye.size)
        drawRect(partColor, topLeft = rightEye.topLeft, size = rightEye.size)

        drawRect(partColor, topLeft = nose.topLeft, size = nose.size)
        drawRect(partColor, topLeft = mouth.topLeft, size = mouth.size)
    })
}

private fun Offset.translate(
    value: Float,
) = translate(
    x = value,
    y = value,
)

private fun Offset.translate(
    x: Float = 0f,
    y: Float = 0f,
) = copy(
    x = this.x + x,
    y = this.y + y,
)
