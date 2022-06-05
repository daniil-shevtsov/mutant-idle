package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Preview(
    widthDp = 800,
    heightDp = 800,
)
@Composable
fun HeadPreview() {
    Box(
        modifier = Modifier.size(800.dp).background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(400.dp), onDraw = {
            val headArea = Rect(
                offset = center.translate(-size.height / 2f),
                size = size,
            )
            drawHead(headArea = headArea)
        })
    }
}

fun DrawScope.drawHead(headArea: Rect) {
    val topHeadArea = headArea
        .shrink(heightPercent = 0.33f)
        .let { area -> area.move(position = headArea.topCenter.translate(y = area.height / 2f)) }
    val middleArea = headArea
        .shrink(heightPercent = 0.33f)
        .let { area -> area.move(position = topHeadArea.bottomCenter.translate(y = area.height / 2f)) }
    val bottomArea = headArea
        .shrink(heightPercent = 0.34f)
        .let { area -> area.move(position = middleArea.bottomCenter.translate(y = area.height / 2f)) }

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

    val supportY = ((topHeadArea.bottomLeft.y - topHeadArea.topLeft.y) * 0.5f) * 2
    val supportX = ((topHeadArea.bottomCenter.x - topHeadArea.bottomLeft.x) * 0.5f) * 2
    val state = BezierState(
        topHeadArea.bottomLeft,
        topHeadArea.bottomRight,
        topHeadArea.bottomCenter.translate(x = -topHeadArea.width, y = -topHeadArea.height),
        topHeadArea.bottomCenter.translate(x = topHeadArea.width, y = -topHeadArea.height),
    )
    clipPath(path = Path().apply {

        drawQuadraticBezier(state)
        lineTo(bottomArea.bottomRight.x, bottomArea.bottomRight.y)
        lineTo(bottomArea.bottomLeft.x, bottomArea.bottomLeft.y)
        lineTo(topHeadArea.bottomLeft.x, topHeadArea.bottomLeft.y)
        close()
    }, clipOp = ClipOp.Intersect) {
        drawBodyPart(head)
        drawBodyPart(BodyPart(topHeadArea.topLeft, topHeadArea.size, Color.Blue))
    }


    drawArea(headArea)
    drawArea(topHeadArea)
    drawArea(middleArea)
    drawArea(bottomArea)

    drawBezierPoints(state)
}
