package com.daniil.shevtsov.idle.core.ui.shapes

import androidx.compose.ui.geometry.Offset
import assertk.assertThat
import assertk.assertions.containsExactly
import org.junit.jupiter.api.Test

internal class OutlinePathCreatorKtTest {

    @Test
    fun `should create correct segments`() {
        assertThat(createSegments(length = 500f, numberOfSegments = 5))
            .containsExactly(0f, 100f, 200f, 300f, 400f, 500f)
    }

    @Test
    fun `should create offsets`() {
        assertThat(createOffsets(segmentXs = listOf(0f, 10f, 20f), y = 10f))
            .containsExactly(
                Offset(x = 0f, y = 10f),
                Offset(x = 10f, y = 10f),
                Offset(x = 20f, y = 10f),
            )
    }

    @Test
    fun `should apply deltas`() {
        assertThat(
            applyDeltas(
                segments = listOf(
                    Offset(0f, 10f),
                    Offset(10f, 10f),
                    Offset(20f, 10f),
                ),
                deltaSize = { 5f },
                deltaSign = ::isEven,
            )
        )
            .containsExactly(
                Offset(x = 0f, y = 5f),
                Offset(x = 10f, y = 15f),
                Offset(x = 20f, y = 5f),
            )
    }


//    fun applyDeltas(): List<Offset> {
//        y = topOffset - delta * 0.5f + delta * randomFloat
//    }

//    @Test
//    fun `should create deltas`() {
//        val segments = listOf(0f, 11f, 20f)
//        assertThat(createDeltas(segments, ::oddEventDelta))
//            .containsExactly(
//                DeltaSegment(0f, -10f),
//                DeltaSegment(11f, 10f),
//                DeltaSegment(20f, -10f),
//            )
//    }

}
