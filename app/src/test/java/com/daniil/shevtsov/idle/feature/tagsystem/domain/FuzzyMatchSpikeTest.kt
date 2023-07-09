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

        val line = say(tags)

        assertThat(line).isEqualTo("Hello, what can I do for you?")
    }

    @Test
    fun `kek2`() {
        val tags = mapOf(
            "time_of_day" to "day",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).isEqualTo("Hey, you are not supposed to be here. Get out!")
    }

    @Test
    fun `kek3`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).isEqualTo("Who is there?! I am armed, get out while you can!")
    }

    @Test
    fun `kek4`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "alien",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).isEqualTo("This is my shop and this is my planet! Get out!")
    }

    @Test
    fun `kek5`() {
        val tags = mapOf(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "demon",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).isEqualTo("In the name of the lord, I say be gone!")
    }

    @Test
    fun `kek6`() {
        val tags = mapOf(
            "time_of_day" to "morning",
            "trespassing" to "false",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).isEqualTo("Morning, what can I do for you?")
    }

    @Test
    fun `kek7`() {
        val tags = mapOf(
            "position" to "ground",
            "posture" to "standing",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).isEqualTo("You stand, doing nothing")
    }

    @Test
    fun `kek8`() {
        val tags = mapOf(
            "position" to "ground",
            "posture" to "lying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).isEqualTo("You lie on the ground, doing nothing")
    }

    @Test
    fun `kek9`() {
        val tags = mapOf(
            "position" to "low-air",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).isEqualTo("You fall to the ground")
    }

    @Test
    fun `kek10`() {
        val tags = mapOf(
            "position" to "low-air",
            "posture" to "flying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).isEqualTo("You fly in low-air")
    }

    @Test
    fun `kek11`() {
        val tags = mapOf(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "current action" to "fly"
        )

        val line = perform(tags)

        assertThat(line).isEqualTo("You fly in low-air")
    }

    private fun perform(tags: Map<String, String>): String {
        return performm(tags).second
    }

    private fun performm(tags: Map<String, String>): Pair<Map<String, String>, String> {
        val lines = listOf(
            listOf("posture" to "standing") to "You stand, doing nothing",
            listOf(
                "posture" to "lying",
                "position" to "ground"
            ) to "You lie on the ground, doing nothing",
            listOf("posture" to "lying") to "You lie, doing nothing",
            listOf("position" to "low-air", "posture" to "flying") to "You fly in low-air",
            listOf("position" to "low-air") to "You fall to the ground",
            listOf("" to "") to "You do nothing",
        )

        val mostSuitableLine = lines
            .filter { (lineTags, _) ->
                lineTags == listOf("" to "") || lineTags.all { (key, value) -> tags[key] == value }
            }
            .maxByOrNull { (lineTags, _) ->
                lineTags.count { (key, value) -> tags[key] == value }
            }?.second.orEmpty()

        return tags to mostSuitableLine
    }

    private fun say(tags: Map<String, String>): String {
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
            listOf("time_of_day" to "morning") to "Morning, what can I do for you?",
            listOf("" to "") to "Hello, what can I do for you?"
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
