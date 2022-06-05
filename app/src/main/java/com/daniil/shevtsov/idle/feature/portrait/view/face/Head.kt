package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.feature.portrait.view.*
import timber.log.Timber

data class BezierViewState(
    val points: BezierState,
    val previousSelectedIndex: Int,
)

private fun Modifier.bezierDragging(
    state: BezierViewState,
    bounds: Rect,
    hackyMutableState: MutableState<BezierViewState>,
//    updateState: (newState: BezierViewState) -> Unit,
) = pointerInput(Unit) {
    detectDragGestures { change, dragAmount ->
        Timber.tag("KEK").d("TRIGGERED")
        val screenBounds = bounds
        var originalState = state.points
        var previousSelectedPointIndex = state.previousSelectedIndex
        if (screenBounds.contains(change.position)) {
            change.consumeAllChanges()
            val oldPoints = originalState.points()
            val selectedPointIndex = when {
                previousSelectedPointIndex != -1 -> {
                    Timber.tag("KEK").d("Reuse position")
                    previousSelectedPointIndex
                }
                else -> {
                    Timber.tag("KEK").d("Choose nearest")
                    oldPoints
                        .minByOrNull { point ->
                            point.distanceTo(change.position)
                        }.let { oldPoints.indexOf(it) }
                }
            }
            if (selectedPointIndex != previousSelectedPointIndex) {
                Timber.tag("KEK").d("Update previous point")
                previousSelectedPointIndex = selectedPointIndex
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
                Timber.tag("KEK").d("Bounds: $bounds")
                Timber.tag("KEK").d("State points: ${state.points.points()}")
                Timber.tag("KEK").d("Old points: ${oldPoints}")
                Timber.tag("KEK").d("New Points: ${newPoints}")
                Timber.tag("KEK").d("coerced points: ${coercedPoints}")
                originalState = coercedPoints.toBezierState()
                hackyMutableState.value = hackyMutableState.value.copy(
                    points = originalState
                )
//                updateState(
//                    state.copy(
//                        points = originalState,
//                        previousSelectedIndex = previousSelectedPointIndex
//                    )
//                )
            }
        }
//        change.consumeAllChanges()
//        val oldPoints = state.points.points()/*.map { pointPercentage ->
//            pointPercentage.times(
//                x = bounds.width,
//                y = bounds.height,
//            )
//        }*/
//        val selectedPointIndex = when {
//            state.previousSelectedIndex != -1 -> {
//                Timber.tag("KEK").d("Reuse position")
//                state.previousSelectedIndex
//            }
//            else -> {
//                Timber.tag("KEK").d("Choose nearest")
//                oldPoints
//                    .minByOrNull { point ->
//                        point.distanceTo(change.position)
//                    }.let { oldPoints.indexOf(it) }
//            }
//        }
//        if (selectedPointIndex != state.previousSelectedIndex) {
//            Timber.tag("KEK").d("Update previous point")
//            updateState(
//                state.copy(
//                    previousSelectedIndex = selectedPointIndex
//                )
//            )
//        }
//        val selectedPoint = oldPoints.getOrNull(selectedPointIndex)
//        if (selectedPoint != null) {
//            val newPoints = oldPoints.mapIndexed { index, point ->
//                if (index == selectedPointIndex) {
//                    point.translate(
//                        x = dragAmount.x,
//                        y = dragAmount.y,
//                    )
//                } else {
//                    point
//                }
//            }
//            val finalPoints = newPoints.map { point ->
//                point/*.coerceIn(bounds)*//*.div(
//                    x = bounds.width,
//                    y = bounds.height
//                )*/
//            }

//            updateState(
//                state.copy(
//                    points = finalPoints.toBezierState()
//                )
//            )
//    }
    }
}

@Composable
fun DraggingComposable() {
    val screenSizeDp = 200.dp
    val screenSize = Size(screenSizeDp.value, screenSizeDp.value)
    val screenBounds = Rect(Offset.Zero, screenSize)
    val originalState = remember {
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
    val percentageState = remember {
        mutableStateOf(
            BezierViewState(
                points = BezierState(
//                    start = Offset(0f, 0.5f),
//                    finish = Offset(1f, 0.5f),
//                    support = Offset(0f, 0f),
//                    support2 = Offset(1f, 0f),
                    start = screenBounds.centerLeft,
                    finish = screenBounds.centerRight,
                    support = screenBounds.topLeft,
                    support2 = screenBounds.topRight,
                ),
                previousSelectedIndex = -1
            )
        )
    }

    Row {
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
                            Timber.tag("KEK").d("TRIGGERED")
                            if (screenBounds.contains(change.position)) {
                                change.consumeAllChanges()
                                val oldPoints = originalState.value.points()
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
                                    val coercedPoints = newPoints.map { point ->
                                        point.coerceIn(screenBounds)
                                    }
                                    Timber.tag("KEK").d("Bounds: $screenBounds")
                                    Timber.tag("KEK")
                                        .d("State points: ${originalState.value.points()}")
                                    Timber.tag("KEK").d("Old points: ${oldPoints}")
                                    Timber.tag("KEK").d("New Points: ${newPoints}")
                                    Timber.tag("KEK").d("coerced points: ${coercedPoints}")
                                    originalState.value = coercedPoints.toBezierState()
                                }
                            }

                        }
                    },
            ) {
                drawRect(Color.Gray, topLeft = screenBounds.topLeft, size = screenBounds.size)
                drawPath(path = Path().apply {
                    drawQuadraticBezier(originalState.value)
                }, color = Color.Black, style = Stroke(width = 3f))
                drawBezierPoints(originalState.value)
            }
        }

        fun updateState(newState: BezierViewState) {
            percentageState.value = newState
        }

        Box(
            modifier = Modifier.size(screenSizeDp).background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(
                modifier = Modifier.background(Color.Cyan)
                    .size(400.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = {
                                Timber.tag("KEK").d("Drag started")
                                updateState(
                                    percentageState.value.copy(
                                        previousSelectedIndex = -1
                                    )
                                )
                            },
                            onDragEnd = {
                                Timber.tag("KEK").d("Drag ended")
                                updateState(
                                    percentageState.value.copy(
                                        previousSelectedIndex = -1
                                    )
                                )
                            },
                            onDragCancel = {
                                Timber.tag("KEK").d("Drag cancelled")
                                updateState(
                                    percentageState.value.copy(
                                        previousSelectedIndex = -1
                                    )
                                )
                            }
                        ) { change, dragAmount ->
                            Timber.tag("KEK").d("TRIGGERED")
                            val previousSelectedPointIndex =
                                percentageState.value.previousSelectedIndex
                            if (screenBounds.contains(change.position)) {
                                change.consumeAllChanges()
                                val originalState = percentageState.value.points
                                val oldPoints = originalState.points()
                                val selectedPointIndex = when {
                                    previousSelectedPointIndex != -1 -> {
                                        Timber.tag("KEK").d("Reuse position")
                                        previousSelectedPointIndex
                                    }
                                    else -> {
                                        Timber.tag("KEK").d("Choose nearest")
                                        oldPoints
                                            .minByOrNull { point ->
                                                point.distanceTo(change.position)
                                            }.let { oldPoints.indexOf(it) }
                                    }
                                }
                                if (selectedPointIndex != previousSelectedPointIndex) {
                                    Timber.tag("KEK").d("Update previous point")
                                    percentageState.value = percentageState.value.copy(
                                        previousSelectedIndex = selectedPointIndex
                                    )
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
                                    Timber.tag("KEK").d("Bounds: $screenBounds")
                                    Timber.tag("KEK")
                                        .d("State points: ${originalState.points()}")
                                    Timber.tag("KEK").d("Old points: ${oldPoints}")
                                    Timber.tag("KEK").d("New Points: ${newPoints}")
                                    Timber.tag("KEK").d("coerced points: ${coercedPoints}")

                                    percentageState.value = percentageState.value.copy(
                                        points = coercedPoints.toBezierState()
                                    )
                                }
                            }

                        }
                    },
            ) {
                val normalState = percentageState.value.copy(
                    points = percentageState.value.points/*.multiply(
                        x = screenBounds.width,
                        y = screenBounds.height
                    )*/
                )
                drawRect(Color.Gray, topLeft = screenBounds.topLeft, size = screenBounds.size)
                drawPath(path = Path().apply {
                    drawQuadraticBezier(normalState.points)
                }, color = Color.Black, style = Stroke(width = 3f))
                drawBezierPoints(normalState.points)
            }
        }
    }

}

@Composable
fun HeadPreviewComposable() {
    val state = remember {
        mutableStateOf(
            BezierViewState(
                points = BezierState(
                    start = Offset(0f, 0.5f),
                    finish = Offset(1f, 0.5f),
                    support = Offset(0f, 0f),
                    support2 = Offset(1f, 1f),
                ),
                previousSelectedIndex = -1,
            )
        )
    }
    Column(modifier = Modifier.background(Color.White)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            state.value.points.points().forEachIndexed { index, point ->
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
                /*.bezierDragging(
                    state = state.value,
                    bounds = canvasBounds,
                    updateState = { newState ->
                        state.value = newState
                    }
                )*/,
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
                        state = state.value.points,
                        onStateChanged = { newPoints ->
                            state.value = state.value.copy(
                                points = newPoints
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
//    HeadPreviewComposable()
    DraggingComposable()
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
