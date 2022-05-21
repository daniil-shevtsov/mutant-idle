package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class OutlinePathCreatorKtTest {

    @Test
    fun `should return rectangle in simple case`() {
        assertThat(createOutlinePath(bounds = Rect(Offset(0f, 0f), Size(10f, 10f))))
            .prop(OutlinePath::points)
            .containsExactly(
                Offset(0f, 0f),
                Offset(10f, 0f),
                Offset(10f, 10f),
                Offset(0f, 10f),
                Offset(0f, 0f),
            )
    }

    @Test
    fun `should create correct segments`() {
        assertThat(createSegments(length = 500f, numberOfSegments = 5))
            .containsExactly(0f, 100f, 200f, 300f, 400f, 500f)
    }

    @Test
    fun `should create deltas`() {
        val segments = listOf(0f, 11f, 20f)
        assertThat(createDeltas(segments, ::oddEvenDeltaGenerator))
            .containsExactly(
                DeltaSegment(0f, Offset(0f, -10f)),
                DeltaSegment(11f, Offset(11f, 10f)),
                DeltaSegment(20f, Offset(20f, -10f)),
            )
    }

}
