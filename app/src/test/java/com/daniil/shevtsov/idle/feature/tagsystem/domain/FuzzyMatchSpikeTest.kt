package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

private typealias Tags = Map<String, String>
private typealias Tag = Pair<String, String>
private typealias Plot = String

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

        assertThat(line).plot().isEqualTo("Hello, what can I do for you?")
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

        assertThat(line).plot().isEqualTo("Hey, you are not supposed to be here. Get out!")
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

        assertThat(line).plot().isEqualTo("Who is there?! I am armed, get out while you can!")
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

        assertThat(line).plot().isEqualTo("This is my shop and this is my planet! Get out!")
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

        assertThat(line).plot().isEqualTo("In the name of the lord, I say be gone!")
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

        assertThat(line).plot().isEqualTo("Morning, what can I do for you?")
    }

    @Test
    fun `kek7`() {
        val tags = mapOf(
            "position" to "ground",
            "posture" to "standing",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You stand, doing nothing")
    }

    @Test
    fun `kek8`() {
        val tags = mapOf(
            "position" to "ground",
            "posture" to "lying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You lie on the ground, doing nothing")
    }

    @Test
    fun `kek9`() {
        val tags = mapOf(
            "position" to "low-air",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You fall to the ground")
    }

    @Test
    fun `kek10`() {
        val tags = mapOf(
            "position" to "low-air",
            "posture" to "flying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You fly in low-air")
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

        assertThat(line).plot().isEqualTo("You start flying")
        assertThat(line).tags()
            .containsAll(
                "posture" to "flying",
                "position" to "low-air",
            )
    }

    @Test
    fun `kek12`() {
        val tags = mapOf(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "current action" to "fly"
        )

        val flying = perform(tags)

        val withoutFlying =
            perform(flying.tags.toMutableMap().apply { put("current action", "stop flying") })
        val finalResult = perform(withoutFlying.tags)

        assertThat(finalResult).plot().isEqualTo("You fall to the ground")
        assertThat(finalResult).tags()
            .containsAll(
                "posture" to "lying",
                "position" to "ground",
            )
    }

    private fun Assert<PerformResult>.plot() = prop(PerformResult::plot)

    private fun Assert<PerformResult>.tags() = prop(PerformResult::tags)

    private fun tags(vararg entries: Tag): Tags = entries.toList().toMap()
    private fun entry(plot: String, tagChange: Tags = mapOf()): LineEntry = LineEntry(
        tags = tagChange,
        plot = plot,
    )

    data class LineEntry(
        val tags: Tags,
        val plot: Plot,
    )

    data class PerformResult(
        val tags: Tags,
        val plot: Plot,
    )

    data class Line(
        val requiredTags: Tags,
        val entry: LineEntry,
    )

    private fun line(
        requiredTags: Tags,
        entry: LineEntry,
    ) = Line(requiredTags, entry)

    private fun perform(tags: Tags): PerformResult {
        val lines = listOf(
            line(
                requiredTags = tags("current action" to "stop flying"),
                entry = entry(
                    "You stop flying",
                    tags("posture" to "standing")
                )
            ),
            line(
                requiredTags = tags("posture" to "standing"),
                entry = entry("You stand, doing nothing")
            ),
            line(
                requiredTags = tags(
                    "posture" to "lying",
                    "position" to "ground"
                ),
                entry = entry("You lie on the ground, doing nothing")
            ),
            line(
                requiredTags = tags("posture" to "lying"),
                entry = entry("You lie, doing nothing")
            ),
            line(
                requiredTags = tags("position" to "low-air", "posture" to "flying"),
                entry = entry("You fly in low-air")
            ),
            line(
                requiredTags = tags("position" to "low-air"),
                entry = entry("You fall to the ground")
            ),
            line(
                requiredTags = tags("current action" to "fly"),
                entry = entry(
                    "You start flying",
                    tags("posture" to "flying", "position" to "low-air")
                )
            ),
            line(requiredTags = tags("" to ""), entry = entry("You do nothing")),
        )

        val sortedEntries = lines
            .filter { line ->
                line.requiredTags == listOf("" to "") || line.requiredTags.all { (key, value) -> tags[key] == value }
            }
            .sortedByDescending { (lineTags, _) ->
                lineTags.count { (key, value) -> tags[key] == value }
            }

        val mostSuitableEntry = sortedEntries.first().entry
        val mostSuitableLine = mostSuitableEntry.plot

        return PerformResult(
            tags = tags + mostSuitableEntry.tags,
            plot = mostSuitableLine,
        )
    }

    private fun say(tags: Tags): PerformResult {
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

        return PerformResult(tags, mostSuitableLine)
    }

}
