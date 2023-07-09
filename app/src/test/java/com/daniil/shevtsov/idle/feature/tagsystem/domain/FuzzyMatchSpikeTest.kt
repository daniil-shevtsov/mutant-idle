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
        val lines = listOf(
            listOf(
                "trespassing" to "true",
                "time_of_day" to "night",
                "appearance" to "demon",
            ) to "In the name of the lord, I say be gone!",
            listOf(
                "trespassing" to "true",
                "time_of_day" to "night",
                "appearance" to "alien",
            ) to "This is my shop and this is my planet! Get out!",
            listOf(
                "trespassing" to "true",
                "time_of_day" to "night",
            ) to "Who is there?! I am armed, get out while you can!",
            listOf(
                "trespassing" to "true",
            ) to "Hey, you are not supposed to be here. Get out!",
            listOf("" to "") to "Morning, what can I do for you?"
        )

        val mostSuitableLine = lines
            .filter { (lineTags, _) ->
               lineTags == listOf("" to "") || lineTags.all { (key, value) -> tags[key] == value }
            }
            .maxByOrNull { (lineTags, _) ->
                lineTags.count { (key, value) -> tags[key] == value }
            }?.second.orEmpty()

        return mostSuitableLine
    }

}
