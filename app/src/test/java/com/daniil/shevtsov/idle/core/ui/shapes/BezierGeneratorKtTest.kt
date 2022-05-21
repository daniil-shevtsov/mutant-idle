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
    fun `should return correct for two points`() {
        assertThat(generateBezier(points = listOf(Offset(0f, 0f), Offset(10f, 10f))))
            .containsExactly(
                BezierPoint(
                    startPoint = Offset(0f, 0f),
                    endPoint = Offset(10f, 10f),
                    startSupportPoint = Offset(5f, 0f),
                    endSupportPoint = Offset(5f, 10f)
                )
            )
    }

    @Test
    fun `should return correct for three points`() {
        assertThat(
            generateBezier(
                points = listOf(
                    Offset(0f, 0f),
                    Offset(10f, 10f),
                    Offset(20f, 20f)
                )
            )
        )
            .containsExactly(
                BezierPoint(
                    startPoint = Offset(0f, 0f),
                    endPoint = Offset(10f, 10f),
                    startSupportPoint = Offset(5f, 0f),
                    endSupportPoint = Offset(5f, 10f)
                ),
                BezierPoint(
                    startPoint = Offset(10f, 10f),
                    endPoint = Offset(20f, 20f),
                    startSupportPoint = Offset(15f, 10f),
                    endSupportPoint = Offset(15f, 20f)
                ),
            )
    }

}
