package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.jupiter.api.Test

internal class BezierGeneratorKtTest {

    @Test
    fun `should return empty for empty`() {
        assertThat(generateBezier(points = emptyList()))
            .isEmpty()
    }

    @Test
    fun `should return empty for one point`() {
        assertThat(generateBezier(points = listOf(Offset(0f, 0f))))
            .isEmpty()
    }

    @Test
    fun `should return correct for two horizontal points`() {
        assertThat(generateBezier(points = listOf(Offset(0f, 0f), Offset(10f, 0f))))
            .containsExactly(
                BezierPoint(
                    startPoint = Offset(0f, 0f),
                    endPoint = Offset(10f, 0f),
                    startSupportPoint = Offset(5f, 0f),
                    endSupportPoint = Offset(5f, 0f)
                )
            )
    }

    @Test
    fun `should return correct for three horizontal points`() {
        assertThat(
            generateBezier(
                points = listOf(
                    Offset(0f, 0f),
                    Offset(10f, 0f),
                    Offset(20f, 0f)
                )
            )
        )
            .containsExactly(
                BezierPoint(
                    startPoint = Offset(0f, 0f),
                    endPoint = Offset(10f, 0f),
                    startSupportPoint = Offset(5f, 0f),
                    endSupportPoint = Offset(5f, 0f)
                ),
                BezierPoint(
                    startPoint = Offset(10f, 0f),
                    endPoint = Offset(20f, 0f),
                    startSupportPoint = Offset(15f, 0f),
                    endSupportPoint = Offset(15f, 0f)
                ),
            )
    }

    @Test
    fun `should return correct for two vertical points`() {
        assertThat(generateBezier(points = listOf(Offset(0f, 0f), Offset(0f, 10f))))
            .containsExactly(
                BezierPoint(
                    startPoint = Offset(0f, 0f),
                    endPoint = Offset(0f, 10f),
                    startSupportPoint = Offset(0f, 5f),
                    endSupportPoint = Offset(0f, 5f)
                )
            )
    }
}
