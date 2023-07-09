package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import org.junit.jupiter.api.Test

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


}

private fun say(tags: Map<String, String>): PerformResult {
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
