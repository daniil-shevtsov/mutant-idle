package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.defaultTagsWithAdditional
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.dialogLine
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import org.junit.jupiter.api.Test

class TagSystemSayTest {
    @Test
    fun `should greet you when you arrive to the shop during the day`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "day",
            "trespassing" to "false",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("Hello, what can I do for you?")
    }

    @Test
    fun `should scold you when you trespass during the day`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "day",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("Hey, you are not supposed to be here. Get out!")
    }

    @Test
    fun `should warn you when you are trespassing at night`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("Who is there?! I am armed, get out while you can!")
    }

    @Test
    fun `should scold alien when alien trespassing`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "alien",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("This is my shop and this is my planet! Get out!")
    }

    @Test
    fun `should scold demon when demon trespassing`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "night",
            "trespassing" to "true",
            "appearance" to "demon",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("In the name of the lord, I say be gone!")
    }

    @Test
    fun `should say good morning in the morning`() {
        val tags = defaultTagsWithAdditional(
            "time_of_day" to "morning",
            "trespassing" to "false",
            "appearance" to "human",
            "location" to "shop",
        )

        val line = say(tags)

        assertThat(line).lastPlot().isEqualTo("Morning, what can I do for you?")
    }

    private fun say(tags: SpikeTags): Game {
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
            emptyList<Pair<String, String>>() to "Hello, what can I do for you?"
        ).map { (tags, plot) ->
            dialogLine(
                text = plot,
                requiredTags = tags.associate { (key, value) ->
                    tagKey(key) to spikeTag(
                        key = tagKey(key),
                        value = tagValue(value)
                    )
                }
            )
        }

        val mostSuitableLine = lines.mostSuitableFor(tags)?.entry?.plot

        return game(
            tags = tags,
            plot = mostSuitableLine?.let(::listOf).orEmpty()
        )
    }
}
