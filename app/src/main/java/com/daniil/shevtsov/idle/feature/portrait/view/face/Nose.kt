package com.daniil.shevtsov.idle.feature.portrait.view.face

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.daniil.shevtsov.idle.feature.portrait.view.*
import kotlin.random.Random

fun DrawScope.drawNose(
    nose: BodyPart,
    previewState: PreviewState,
) {
    val noseArea = nose.toRect()
    val dorsumArea = noseArea
        .shrink(
            widthPercent = Random.nextFloatInRange(
                min = 0.3f,
                max = 0.8f,
            )
        )
    val nostrilsArea = noseArea
        .shrink(heightPercent = 0.25f)
        .let {
            it.move(
                nose.position.translate(
                    x = it.size.width / 2,
                    y = nose.size.height - it.size.height
                )
            )
        }
    val tipArea = Rect(
        dorsumArea.bottomLeft.translate(
            y = -(dorsumArea.bottomLeft.y - nostrilsArea.bottomLeft.y)
        ),
        Size(
            width = dorsumArea.width,
            height = dorsumArea.bottomLeft.y - nostrilsArea.bottomLeft.y
        )
    )
    val dorsumSize = Size(
        dorsumArea.width,
        dorsumArea.height - tipArea.height
    )
    val dorsum = BodyPart(
        position = dorsumArea.topCenter.translate(
            x = -dorsumSize.width / 2f
        ),
        size = dorsumSize,
        color = Color.LightGray
    )

    val leftNostrilArea = Rect(
        nostrilsArea.topLeft,
        Size(
            width = (dorsumArea.left - nostrilsArea.left),
            height = nostrilsArea.height,
        )
    )
    val rightNostrilArea = Rect(
        nostrilsArea.topRight.translate(
            x = -(nostrilsArea.right - dorsumArea.right)
        ),
        Size(
            width = nostrilsArea.right - dorsumArea.right,
            height = nostrilsArea.height,
        )
    )
    val nostrilsSupportY = Random.nextFloatInRange(
        min = 0f,
        max = nostrilsArea.height
    )
    val nostrilWidth = Random.nextFloatInRange(
        min = (nostrilsArea.width - dorsumArea.width) / 8f,
        max = (nostrilsArea.width - dorsumArea.width) / 2f,
    )

    val leftNostril = BezierState(
        start = leftNostrilArea.topRight,
        finish = leftNostrilArea.bottomRight,
        support = leftNostrilArea.topRight
            .translate(
                x = -nostrilWidth * 2,
                y = nostrilsSupportY,
            ),
    )

    val rightNostril = BezierState(
        start = rightNostrilArea.topLeft,
        finish = rightNostrilArea.bottomLeft,
        support = rightNostrilArea.topLeft
            .translate(
                x = nostrilWidth * 2,
                y = nostrilsSupportY
            ),
    )

    val tipHeight = Random.nextFloatInRange(
        min = tipArea.height * 0.1f,
        max = tipArea.height,
    )
    val noseTip = BezierState(
        start = tipArea.topLeft,
        finish = tipArea.topRight,
        support = tipArea.topCenter.translate(y = tipHeight * 2)
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(rightNostril)
            close()
        },
        Color.LightGray,
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(leftNostril)
            close()
        },
        Color.LightGray,
    )

    drawPath(
        path = Path().apply {
            drawQuadraticBezier(noseTip)
            close()
        },
        Color.LightGray,
    )
    drawBodyPart(dorsum)

    if (previewState.shouldShowNoseAreas) {
        drawArea(noseArea)
        drawArea(dorsumArea)
        drawArea(nostrilsArea)
        drawArea(leftNostrilArea)
        drawArea(rightNostrilArea)
        drawArea(tipArea)
        drawBezierPoints(leftNostril)
        drawBezierPoints(rightNostril)
        drawBezierPoints(noseTip)
    }
}

