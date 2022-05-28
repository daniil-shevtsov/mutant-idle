package com.daniil.shevtsov.idle.feature.portrait.view

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import assertk.all
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

internal class PortaitComposableKtTest {

    @Test
    fun `should translate correctly`() {
        val original = Offset(100f, 200f)
        val translated = original.translate(
            x = 100f,
            y = 200f,
        )
        assertThat(translated)
            .isEqualTo(Offset(200f, 400f))
    }

    @Test
    fun `should shrink rect correctly`() {
        val original = Rect(
            topLeft = Offset(100f, 200f),
            bottomRight = Offset(300f, 500f),
        )

        val translated = original.shrink(
            widthPercent = 0.25f,
            heightPercent = 0.1f,
        )

        assertThat(translated)
            .all {
                prop(Rect::topLeft).isEqualTo(Offset(150f, 230f))
                prop(Rect::topRight).isEqualTo(Offset(250f, 230f))
                prop(Rect::bottomLeft).isEqualTo(Offset(150f, 470f))
                prop(Rect::bottomRight).isEqualTo(Offset(250f, 470f))
            }
    }

    @Test
    fun `should move rect correctly`() {
        val original = Rect(
            topLeft = Offset(0f, 0f),
            bottomRight = Offset(400f, 600f),
        )

        val moved = original.move(
            position = Offset(500f, 500f),
            anchor = Anchor.Center
        )

        assertThat(moved)
            .all {
                prop(Rect::topLeft).isEqualTo(Offset(300f, 200f))
                prop(Rect::bottomRight).isEqualTo(Offset(700f, 800f))
            }
    }

}
