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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "posture" to "standing",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You stand, doing nothing")
    }

    @Test
    fun `kek8`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "posture" to "lying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You lie on the ground, doing nothing")
    }

    @Test
    fun `kek9`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot()
            .isEqualTo("You fall to the ground, breaking every bone in your body")
    }

    @Test
    fun `kek10`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "posture" to "flying",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).plot().isEqualTo("You fly in low-air")
    }

    @Test
    fun `kek11`() {
        val tags = defaultTagsWithAdditional(
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
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "current action" to "fly",
            "indestructible" to "true",
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
                "health" to "100"
            )
    }

    @Test
    fun `kek13`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "current action" to "fly",
        )

        val flying = perform(tags)

        val withoutFlying =
            perform(flying.tags.toMutableMap().apply { put("current action", "stop flying") })
        val finalResult = perform(withoutFlying.tags)

        assertThat(finalResult).plot()
            .isEqualTo("You fall to the ground, breaking every bone in your body")
        assertThat(finalResult).tags()
            .containsAll(
                "posture" to "lying",
                "position" to "ground",
                "bones" to "broken",
                "health" to "0",
            )
    }

    @Test
    fun `kek13_2`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "health" to "0",
        )

        val dead = perform(tags)

        assertThat(dead).plot()
            .isEqualTo("You have died")
        assertThat(dead).tags()
            .containsAll(
                "health" to "0",
                "life" to "dead",
            )
    }

    @Test
    fun `kek14`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
            "immortality" to "true",
            "current action" to "fly",
        )

        val flying = perform(tags)

        val withoutFlying =
            perform(flying.tags.toMutableMap().apply { put("current action", "stop flying") })
        val brokenBones = perform(withoutFlying.tags)
        val cantMove = perform(brokenBones.tags)
        assertThat(cantMove).plot().isEqualTo("Now you can't move")
        assertThat(cantMove).tags()
            .containsAll(
                "mobile" to "false",
            )
        val finalResult =
            perform(cantMove.tags.toMutableMap().apply { put("current action", "stand") })

        assertThat(finalResult).plot().isEqualTo("You can't get up")
        assertThat(finalResult).tags()
            .containsAll(
                "posture" to "lying",
                "position" to "ground",
                "bones" to "broken",
            )
    }

    @Test
    fun `kek15`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "immortality" to "true",
            "ability" to "regeneration",
            "current action" to "fly",
        )

        val flying = perform(tags)

        val withoutFlying =
            perform(flying.tags.toMutableMap().apply { put("current action", "stop flying") })
        val brokenBones = perform(withoutFlying.tags)
        val cantMove = perform(brokenBones.tags)
        val regenerated = perform(cantMove.tags.withAdditional("current action" to "regenerate"))
        assertThat(regenerated).plot().isEqualTo("You regenerate to full health")
        assertThat(regenerated).tags()
            .containsAll(
                "posture" to "lying",
                "position" to "ground",
                "bones" to "okay",
                "mobile" to "true", //TODO: What if you were not mobile because you were tied? With current system regeneration would free you
            )
        val finalResult = perform(regenerated.tags.withAdditional("current action" to "stand"))

        assertThat(finalResult).plot().isEqualTo("You get up")
        assertThat(finalResult).tags()
            .containsAll(
                "posture" to "standing",
                "position" to "ground",
                "bones" to "okay",
                "mobile" to "true",
            )
    }

    private fun Assert<PerformResult>.plot() = prop(PerformResult::plot)

    private fun Assert<PerformResult>.tags() = prop(PerformResult::tags)

    private fun tags(vararg entries: Tag): Tags = entries.toList().toMap()
    private fun entry(plot: String, tagChange: Tags = mapOf(), weight: Float = 1.0f): LineEntry =
        LineEntry(
            tags = tagChange,
            plot = plot,
            weight = weight,
        )

    data class LineEntry(
        val tags: Tags,
        val plot: Plot,
        val weight: Float,
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
                requiredTags = tags("mobile" to "true", "bones" to "broken"),
                entry = entry(
                    "Now you can't move",
                    tagChange = tags("mobile" to "false"),
                    weight = 1000f,
                ),
            ),
            line(
                requiredTags = tags(
                    "immortality" to "!true",
                    "life" to "alive",
                    "health" to "0"
                ),
                entry = entry(
                    "You have died",
                    tagChange = tags("life" to "dead"),
                    weight = 1000f,
                ),
            ),
            line(
                requiredTags = tags("current action" to "stand", "mobile" to "false"),
                entry = entry(
                    "You can't get up",
                )
            ),
            line(
                requiredTags = tags(
                    "current action" to "stand",
                    "posture" to "lying",
                    "mobile" to "true",
                ),
                entry = entry(
                    "You get up",
                    tags("posture" to "standing")
                )
            ),
            line(
                requiredTags = tags(
                    "current action" to "regenerate",
                    "ability" to "regeneration",
                ),
                entry = entry(
                    "You regenerate to full health",
                    tags(
                        "bones" to "okay",
                        "mobile" to "true"//TODO: this should happen separately because the bones are healed
                    )
                )
            ),
            line(
                requiredTags = tags("current action" to "stand", "mobile" to "true"),
                entry = entry(
                    "You are now standing",
                    tags("posture" to "standing")
                )
            ),
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
                requiredTags = tags("position" to "low-air", "indestructible" to "true"),
                entry = entry(
                    "You fall to the ground",
                    tagChange = tags("position" to "ground", "posture" to "lying")
                ),
            ),
            line(
                requiredTags = tags("position" to "low-air"),
                entry = entry(
                    "You fall to the ground, breaking every bone in your body",
                    tagChange = tags(
                        "position" to "ground",
                        "posture" to "lying",
                        "bones" to "broken",
                        "health" to "0",
                    )
                ),
            ),
            line(
                requiredTags = tags("current action" to "fly"),
                entry = entry(
                    "You start flying",
                    tags("posture" to "flying", "position" to "low-air")
                )
            ),
            line(requiredTags = tags("" to ""), entry = entry("You do nothing")),
        ).map { line ->
            when (line.requiredTags.containsKey("current action")) {
                true -> line.copy(
                    entry = line.entry.copy(
                        weight = line.entry.weight + 100f
                    )
                )

                false -> line
            }
        }

        val sortedEntries = lines
            .filter { line ->
                line.requiredTags == listOf("" to "") || line.requiredTags.all { (requiredTag, requiredTagValue) ->
                    val currentTags = tags
                    val currentValue = currentTags[requiredTag]

                    when {
                        requiredTagValue.contains('!') -> currentValue != requiredTagValue.drop(1)
                        else -> currentValue == requiredTagValue
                    }
                }
            }
            .sortedWith(
                compareBy(
                    { (_, entry) -> entry.weight },
                    { (lineTags, _) ->
                        lineTags.count { (key, value) -> tags[key] == value }
                    }
                )
            ).reversed()

        val mostSuitableEntry = sortedEntries.first().entry
        val mostSuitableLine = mostSuitableEntry.plot

        return PerformResult(
            tags = tags.toMutableMap().apply { remove("current action") } + mostSuitableEntry.tags,
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


    private fun createDefaultTags() = mapOf(
        "mobile" to "true",
        "health" to "100",
        "life" to "alive",
    )

    private fun defaultTagsWithAdditional(vararg tags: Tag): Tags =
        createDefaultTags().withAdditional(*tags)

    private fun Tags.withAdditional(vararg tags: Tag): Tags = this + tags.toMap()
}
