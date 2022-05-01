package com.daniil.shevtsov.idle.core.presentation.formatting

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FormattingExtensionsTest {
    @Test
    fun `zero for zero`() {
        val originalValue = 0.0
        val formattedValue = originalValue.formatRound(digits = 3)
        assertThat(formattedValue).isEqualTo("0")
    }

    @Test
    fun `correct digits for one digit`() {
        val originalValue = 0.1
        val formattedValue = originalValue.formatRound(digits = 1)
        assertThat(formattedValue).isEqualTo("0.1")
    }

    @Test
    fun `correct digits for three digits`() {
        val originalValue = 0.001
        val formattedValue = originalValue.formatRound(digits = 3)
        assertThat(formattedValue).isEqualTo("0.001")
    }
}
