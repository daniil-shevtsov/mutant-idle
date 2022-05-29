package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

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

fun DrawScope.drawBodyPart(part: BodyPart) {
    drawRect(part.color, topLeft = part.position, size = part.size)
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
