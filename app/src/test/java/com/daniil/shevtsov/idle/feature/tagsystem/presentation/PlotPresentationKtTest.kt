package com.daniil.shevtsov.idle.feature.tagsystem.presentation

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.plot
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PlotPresentationTest {
    @Test
    fun `should return empty for empty`() {
        val presentation = presentPlot(plot = emptyList())
        assertThat(presentation).isEmpty()
    }

    @Test
    fun `should return one for one`() {
        val presentation = presentPlot(plot = listOf(plot("lol")))
        assertThat(presentation).containsExactly("lol")
    }

    @Test
    fun `should squash two duplicates`() {
        val presentation = presentPlot(plot = listOf(plot("lol"), plot("lol")))
        assertThat(presentation).containsExactly("lol (x2)")
    }

    @Test
    fun `should squash two duplicates after one element`() {
        val presentation = presentPlot(
            plot = listOf(plot("cheburek"), plot("lol"), plot("lol")),
            reversed = false,
        )
        assertThat(presentation).containsExactly(
            "cheburek",
            "lol (x2)",
        )
    }

    @Test
    fun `should squash two duplicates before one element`() {
        val presentation = presentPlot(
            plot = listOf(plot("lol"), plot("lol"), plot("cheburek")),
            reversed = false,
        )
        assertThat(presentation).containsExactly(
            "lol (x2)",
            "cheburek",
        )
    }

    @Test
    fun `should reverse order when true`() {
        val presentation = presentPlot(
            plot = listOf(plot("lol"), plot("kek"), plot("cheburek")),
            reversed = true,
        )
        assertThat(presentation).containsExactly(
            "cheburek",
            "kek",
            "lol",
        )
    }

    @Test
    fun `should reverse order with duplicates when true`() {
        val presentation = presentPlot(
            plot = listOf(plot("lol"), plot("lol"), plot("cheburek")),
            reversed = true,
        )
        assertThat(presentation).containsExactly(
            "cheburek",
            "lol (x2)",
        )
    }

    @Test
    fun `should reverse by defaut`() {
        val presentation = presentPlot(plot = listOf(plot("lol"), plot("kek"), plot("cheburek")))
        assertThat(presentation).containsExactly(
            "cheburek",
            "kek",
            "lol",
        )
    }
}
