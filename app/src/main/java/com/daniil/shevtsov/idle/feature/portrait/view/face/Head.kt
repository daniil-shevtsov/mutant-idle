package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.*
import timber.log.Timber

data class BezierViewState(
    val kek: String = ""
)

@Composable
fun DraggingComposable() {
    val screenSizeDp = 400.dp
    val screenSize = Size(screenSizeDp.value, screenSizeDp.value)
    val screenBounds = Rect(Offset.Zero, screenSize)
    val state = remember {
        mutableStateOf(
            BezierState(
                start = screenBounds.centerLeft,
                finish = screenBounds.centerRight,
                support = screenBounds.topLeft,
                support2 = screenBounds.topRight,
            )
        )
    }
    val previousSelectedPointIndex = remember { mutableStateOf(-1) }
    Box(
        modifier = Modifier.size(screenSizeDp).background(Color.White),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.background(Color.Blue)
                .size(400.dp)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            Timber.tag("KEK").d("Drag started")
                            previousSelectedPointIndex.value = -1
                        },
                        onDragEnd = {
                            Timber.tag("KEK").d("Drag ended")
                            previousSelectedPointIndex.value = -1
                        },
                        onDragCancel = {
                            Timber.tag("KEK").d("Drag cancelled")
                            previousSelectedPointIndex.value = -1
                        }
                    ) { change, dragAmount ->
                        change.consumeAllChanges()
                        val oldPoints = state.value.points()
                        val selectedPointIndex = when {
                            previousSelectedPointIndex.value != -1 -> {
                                Timber.tag("KEK").d("Reuse position")
                                previousSelectedPointIndex.value
                            }
                            else -> {
                                Timber.tag("KEK").d("Choose nearest")
                                oldPoints
                                    .minByOrNull { point ->
                                        point.distanceTo(change.position)
                                    }.let { oldPoints.indexOf(it) }
                            }
                        }
                        if (selectedPointIndex != previousSelectedPointIndex.value) {
                            Timber.tag("KEK").d("Update previous point")
                            previousSelectedPointIndex.value = selectedPointIndex
                        }
                        val selectedPoint = oldPoints.getOrNull(selectedPointIndex)
                        if (selectedPoint != null) {
                            val newPoints = oldPoints.mapIndexed { index, point ->
                                if (index == oldPoints.indexOf(selectedPoint)) {
                                    point.translate(
                                        x = dragAmount.x,
                                        y = dragAmount.y,
                                    )
                                } else {
                                    point
                                }
                            }
                            state.value = newPoints.map { point ->
                                point.coerceIn(screenBounds)
                            }.toBezierState()
                        }
                    }
                },
        ) {
            drawRect(Color.Gray, topLeft = screenBounds.topLeft, size = screenBounds.size)
            drawPath(path = Path().apply {
                drawQuadraticBezier(state.value)
            }, color = Color.Black, style = Stroke(width = 3f))
            drawBezierPoints(state.value)
        }
    }
}

@Preview(
    widthDp = 800,
    heightDp = 800,
)
@Composable
fun HeadPreview() {
    val state = remember {
        mutableStateOf(
            BezierState(
                start = Offset(0f, 0.5f),
                finish = Offset(1f, 0.5f),
                support = Offset(0f, 0f),
                support2 = Offset(1f, 1f),
            )
        )
    }
    Box(
        modifier = Modifier.size(800.dp).background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(400.dp), onDraw = {
            val headArea = Rect(
                offset = center.translate(-size.height / 2f),
                size = size,
            )
            drawRect(
                Color.DarkGray,
                topLeft = center.translate(-size.width / 2f, -size.height / 2f),
                size = size
            )
            drawHead(
                headArea = headArea,
                state = state.value,
                onStateChanged = { newState ->
                    state.value = newState
                }
            )
        })
    }
}

fun DrawScope.drawHead(
    headArea: Rect,
    state: BezierState,
    onStateChanged: (state: BezierState) -> Unit,
) {
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
    val pixelState = state.multiply(
        x = topHeadArea.width,
        y = topHeadArea.height,
    )
    clipPath(path = Path().apply {

        drawQuadraticBezier(pixelState)
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

    drawBezierPoints(pixelState)
}
