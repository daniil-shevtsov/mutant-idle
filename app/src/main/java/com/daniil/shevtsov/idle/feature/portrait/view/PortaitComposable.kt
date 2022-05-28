package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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

        val axisSize = 5f
        val axisColor = Color.Blue
        val horizontalAxis = Rect(
            screenArea.centerLeft.copy(y = screenArea.centerLeft.y - axisSize),
            screenArea.centerRight.copy(y = screenArea.centerLeft.y + axisSize),
        )
        val verticalAxis = Rect(
            screenArea.topCenter.copy(x = screenArea.topCenter.x - axisSize),
            screenArea.bottomCenter.copy(x = screenArea.bottomCenter.x + axisSize),
        )

        drawRect(axisColor, topLeft = horizontalAxis.topLeft, size = horizontalAxis.size)
        drawRect(axisColor, topLeft = verticalAxis.topLeft, size = verticalAxis.size)

    })
}
