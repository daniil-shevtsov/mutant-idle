package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.feature.portrait.view.*
import timber.log.Timber

data class HeadViewState(
    val topArea: BezierViewState,
    val bottomArea: BezierViewState,
)

data class BezierViewState(
    val points: BezierState,
    val previousSelectedIndex: Int,
)

private fun Modifier.bezierDragging(
    percentageStateValue: HeadViewState,
    screenBounds: Rect,
    updateState: (newState: HeadViewState) -> Unit,
) = composed {
    val percentageStateValue by rememberUpdatedState(percentageStateValue)

    pointerInput(Unit) {
        detectDragGestures(
            onDragStart = {
                Timber.tag("UPDATE-INDEX").d("Drag started")
                updateState(
                    percentageStateValue.copy(
                        topArea = percentageStateValue.topArea.copy(
                            previousSelectedIndex = -1
                        ),
                    )
                )
            },
            onDragEnd = {
                Timber.tag("UPDATE-INDEX").d("Drag ended")
                updateState(
                    percentageStateValue.copy(
                        topArea = percentageStateValue.topArea.copy(
                            previousSelectedIndex = -1
                        ),
                    )
                )
            },
            onDragCancel = {
                Timber.tag("UPDATE-INDEX").d("Drag cancelled")
                updateState(
                    percentageStateValue.copy(
                        topArea = percentageStateValue.topArea.copy(
                            previousSelectedIndex = -1
                        ),
                    )
                )
            }
        ) { change, dragAmount ->
            change.consumeAllChanges()


            updateState(
                doEverything(
                    percentageStateValue,
                    screenBounds,
                    change,
                    dragAmount
                )
            )
        }
    }
}

fun doEverything(
    percentageStateValue: HeadViewState,
    screenBounds: Rect,
    change: PointerInputChange,
    dragAmount: Offset,
): HeadViewState {
    val previousSelectedPointIndex =
        percentageStateValue.topArea.previousSelectedIndex
    val originalState = percentageStateValue.topArea.points
    val oldPoints = originalState.points().map { point ->
        point.times(
            x = screenBounds.width,
            y = screenBounds.height,
        )
    }
    val selectedPointIndex = when {
        previousSelectedPointIndex != -1 -> {
            Timber.tag("KEK").d("Reuse position $previousSelectedPointIndex")
            previousSelectedPointIndex
        }
        else -> {
            Timber.tag("UPDATE-INDEX")
                .d(" index is $previousSelectedPointIndex Choose nearest")
            oldPoints
                .minByOrNull { point ->
                    point.distanceTo(change.position)
                }.let { oldPoints.indexOf(it) }
        }
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
        val coercedPoints = newPoints.map { point ->
            point.coerceIn(screenBounds)
        }
        val finalPoints = coercedPoints.map { point ->
            point.div(
                x = screenBounds.width,
                y = screenBounds.height
            )
        }

        return percentageStateValue.copy(
            topArea = percentageStateValue.topArea.copy(
                points = finalPoints.toBezierState(),
                previousSelectedIndex = selectedPointIndex,
            )
        )
    } else {
        return percentageStateValue
    }
}


@Composable
fun HeadPreviewComposable() {
    var state by remember {
        mutableStateOf(
            HeadViewState(
                topArea = BezierViewState(
                    points = BezierState(
                        start = Offset(0f, 0.5f),
                        finish = Offset(1f, 0.5f),
                        support = Offset(0f, 0f),
                        support2 = Offset(1f, 0f),
                    ),
                    previousSelectedIndex = -1,
                ),
                bottomArea = BezierViewState(
                    points = BezierState(
                        start = Offset(0f, 0.5f),
                        finish = Offset(1f, 0.5f),
                        support = Offset(0f, 1f),
                        support2 = Offset(1f, 1f),
                    ),
                    previousSelectedIndex = -1,
                )
            )
        )
    }
    Column(modifier = Modifier.background(Color.White)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            state.topArea.points.points().forEachIndexed { index, point ->
                val title = when (index) {
                    0 -> "Start"
                    1 -> "Finish"
                    2 -> "Support1"
                    3 -> "Support2"
                    else -> "Unknown"
                }
                Text(text = "$title: $point", fontSize = 18.sp)
            }
        }
        Box(
            modifier = Modifier.size(800.dp).background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            val canvasSize = with(LocalDensity.current) { 400.dp.toPx() }
            val canvasBounds = Rect(
                offset = Offset.Zero,
                size = Size(canvasSize, canvasSize)
            )
            Canvas(
                modifier = Modifier
                    .size(400.dp)
                    .background(Color.LightGray)
                    .bezierDragging(
                        percentageStateValue = state,
                        screenBounds = canvasBounds,
                        updateState = { newState ->
                            state = newState
                        }
                    ),
                onDraw = {
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
                        state = state,
                        onStateChanged = { newPoints ->
                            state = state.copy(
                                topArea = state.topArea.copy(
                                    points = newPoints
                                )
                            )
                        }
                    )
                })
        }
    }
}

@Preview(
    widthDp = 800,
    heightDp = 800,
)
@Composable
fun HeadPreview() {
    HeadPreviewComposable()
//    DraggingComposable()
}

fun DrawScope.drawHead(
    headArea: Rect,
    state: HeadViewState,
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

    val topAreaInPixels = state.topArea.points.multiply(
        x = topHeadArea.width,
        y = topHeadArea.height,
    )
    val debugChinWidth = (topAreaInPixels.finish.x - topAreaInPixels.start.x) / 2
    val debugChin = Rect(
        topLeft = Offset(topAreaInPixels.start.x + debugChinWidth / 2, bottomArea.bottom),
        bottomRight = Offset(
            topAreaInPixels.finish.x - debugChinWidth / 2,
            bottomArea.bottom
        )
    )
    drawPath(
        path = Path().apply {
            drawQuadraticBezier(topAreaInPixels)
            lineTo(topAreaInPixels.finish.x, middleArea.bottom)
            lineTo(debugChin.right, debugChin.bottom)
            lineTo(debugChin.left, debugChin.bottom)
            lineTo(topAreaInPixels.start.x, middleArea.bottom)
            lineTo(topAreaInPixels.start.x, topAreaInPixels.start.y)
        },
        color = Color.Cyan,
        style = Fill,
    )
    drawPath(
        path = Path().apply {
            drawQuadraticBezier(topAreaInPixels)
        },
        color = Color.Yellow,
        style = Stroke(width = 3f),
    )

    drawArea(headArea)
    drawArea(topHeadArea)
    drawArea(middleArea)
    drawArea(bottomArea)

    drawBezierPoints(topAreaInPixels)
}
