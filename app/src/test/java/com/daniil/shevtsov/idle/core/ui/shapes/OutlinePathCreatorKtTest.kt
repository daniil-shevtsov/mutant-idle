package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.jupiter.api.Test

internal class OutlinePathCreatorKtTest {

    @Test
    fun `should return rectangle in simple case`() {
        assertThat(createOutlinePath(bounds = Rect(Offset(0f, 0f), Size(10f, 10f))))
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

}
