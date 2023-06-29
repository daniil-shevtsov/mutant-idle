package com.daniil.shevtsov.idle.feature.flavor

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import org.junit.jupiter.api.Test

internal class SplitByTokensTest {
    @Test
    fun `should return empty for empty`() {
        val original = ""
        val split = original.splitByTokens(prefix = '8', postfix = '9')
        assertThat(split).isEmpty()
    }

    @Test
    fun `should return original when no tokens`() {
        val original = "lol"
        val split = original.splitByTokens(prefix = '8', postfix = '9')
        assertThat(split).containsExactly(original)
    }

    @Test
    fun `should split when token at the start`() {
        val original = "8kek9lol"
        val split = original.splitByTokens(prefix = '8', postfix = '9')
        assertThat(split).containsExactly(
            "8kek9",
            "lol",
        )
    }
}
