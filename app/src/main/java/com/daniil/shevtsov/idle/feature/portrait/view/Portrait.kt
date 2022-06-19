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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.feature.portrait.view.face.drawNose
import com.daniil.shevtsov.idle.feature.portrait.view.face.drawPortrait

@Preview(
    widthDp = 800,
    heightDp = 800
)
@Composable
fun PortraitPreview() {
    val previewSize = 400.dp

    val previewMode = PreviewMode.Portaits

    val state = PreviewState(
        mode = previewMode,
        shouldShowFaceAreas = false,
        shouldShowNoseAreas = false,
        shouldShowEyeAreas = false,
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

@Composable
fun Portrait(
    previewState: PreviewState,
    modifier: Modifier = Modifier,
) {
    Canvas(modifier = modifier, onDraw = {
        drawPortrait(previewState)
    })
}

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


enum class PreviewMode {
    Portaits,
    Nose,
}

data class PreviewState(
    val mode: PreviewMode,
    val shouldShowFaceAreas: Boolean,
    val shouldShowNoseAreas: Boolean,
    val shouldShowEyeAreas: Boolean,
)




