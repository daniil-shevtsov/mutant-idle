package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.BodyPart
import com.daniil.shevtsov.idle.feature.portrait.view.drawArea
import com.daniil.shevtsov.idle.feature.portrait.view.drawBodyPart
import com.daniil.shevtsov.idle.feature.portrait.view.translate

@Preview
@Composable
fun HeadPreview() {
    Canvas(modifier = Modifier.size(400.dp), onDraw = {
        val headArea = Rect(
            offset = center.translate(-size.height / 2f),
            size = size,
        )
        drawHead(headArea = headArea)
    })
}

fun DrawScope.drawHead(headArea: Rect) {
    val headSize = Size(
        width = headArea.width * 0.7f,
        height = headArea.height,
    )
    val head = BodyPart(
        position = headArea.center.translate(
            x = -headSize.width / 2,
            y = -headSize.height / 2,
        ),
        size = headSize,
        color = Color.Gray
    )

    drawBodyPart(head)
    drawArea(headArea)

}
