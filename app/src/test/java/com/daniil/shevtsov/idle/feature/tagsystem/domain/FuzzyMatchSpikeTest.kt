package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class FuzzyMatchSpikeTest {

    @Test
    fun `test`() {
        val tags = mapOf(
            "time_of_day" to "day",
            "trespassing" to "false",
            "appearance" to "human",
            "location" to "shop",
        )

        val expected = match(tags)

        assertThat(expected).isEqualTo("Morning, what can I do for you?")
    }

    @Test
    fun `kek2`() {
        val tags = mapOf(
            "time_of_day" to "day",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val expected = match(tags)

        assertThat(expected).isEqualTo("Hey, you are not supposed to be here. Get out!")
    }

    @Test
    fun `kek3`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val expected = match(tags)

        assertThat(expected).isEqualTo("Who is there?! I am armed, get out while you can!")
    }

    @Test
    fun `kek4`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "alien",
            "location" to "shop",
        )

        val expected = match(tags)

        assertThat(expected).isEqualTo("This is my shop and this is my planet! Get out!")
    }

    @Test
    fun `kek5`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "demon",
            "location" to "shop",
        )

        val expected = match(tags)

        assertThat(expected).isEqualTo("In the name of the lord, I say be gone!")
    }

    private fun match(tags: Map<String, String>): String {
        return when {
            tags["trespassing"] == "true" && tags["time_of_day"] == "night" && tags["appearance"] == "demon" -> "In the name of the lord, I say be gone!"
            tags["trespassing"] == "true" && tags["time_of_day"] == "night" && tags["appearance"] == "alien" -> "This is my shop and this is my planet! Get out!"
            tags["trespassing"] == "true" && tags["time_of_day"] == "night" -> "Who is there?! I am armed, get out while you can!"
            tags["trespassing"] == "true" -> "Hey, you are not supposed to be here. Get out!"
            else -> "Morning, what can I do for you?"
        }
    }

}
