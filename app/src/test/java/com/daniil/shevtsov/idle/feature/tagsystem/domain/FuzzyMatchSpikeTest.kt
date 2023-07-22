package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.index
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.defaultTagsWithAdditional
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.dialogLine
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.withAdditional
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

        assertThat(line).lastPlot().isEqualTo("Hello, what can I do for you?")
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

        assertThat(line).lastPlot().isEqualTo("Hey, you are not supposed to be here. Get out!")
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

        assertThat(line).lastPlot().isEqualTo("Who is there?! I am armed, get out while you can!")
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

        assertThat(line).lastPlot().isEqualTo("This is my shop and this is my planet! Get out!")
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

        assertThat(line).lastPlot().isEqualTo("In the name of the lord, I say be gone!")
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

        assertThat(line).lastPlot().isEqualTo("Morning, what can I do for you?")
    }

    @Test
    fun `should stand doing nothing when wait while standing`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "posture" to "standing",
            "appearance" to "human",
        )

        val line = perform(tags.withAdditional("current action" to "wait"))

        assertThat(line).lastPlot().isEqualTo("You stand, doing nothing")
    }

    @Test
    fun `kek8`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "posture" to "lying",
            "appearance" to "human",
        )

        val line = perform(tags.withAdditional("current action" to "wait"))

        assertThat(line).lastPlot().isEqualTo("You lie on the ground, doing nothing")
    }

    @Test
    fun `kek9`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).lastPlot()
            .isEqualTo("You fall to the ground, breaking every bone in your body")
    }

    @Test
    fun `kek10`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "posture" to "flying",
            "appearance" to "human",
        )

        val line = perform(tags.withAdditional("current action" to "wait"))

        assertThat(line).lastPlot().isEqualTo("You fly in low-air")
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

        assertThat(line).lastPlot().isEqualTo("You start flying")
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
            perform(flying.tags.withAdditional("current action" to "stop flying"))
        val finalResult = perform(withoutFlying.tags)

        assertThat(finalResult).lastPlot().isEqualTo("You fall to the ground")
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
            perform(flying.tags.withAdditional("current action" to "stop flying"))
        val finalResult = perform(withoutFlying.tags)

        assertThat(finalResult).lastPlot()
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

        assertThat(dead).lastPlot()
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
            perform(flying.tags.withAdditional("current action" to "stop flying"))
        val brokenBones = perform(withoutFlying.tags)
        val cantMove = perform(brokenBones.tags)
        assertThat(cantMove).lastPlot().isEqualTo("Now you can't move")
        assertThat(cantMove).tags()
            .containsAll(
                "mobile" to "false",
            )
        val finalResult =
            perform(cantMove.tags.withAdditional("current action" to "stand"))

        assertThat(finalResult).lastPlot().isEqualTo("You can't get up")
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
            "ability" to "[flight,regeneration]",
            "current action" to "fly",
        )

        val flying = perform(tags)

        val withoutFlying =
            perform(flying.tags.withAdditional("current action" to "stop flying"))
        val brokenBones = perform(withoutFlying.tags)
        val cantMove = perform(brokenBones.tags)
        val regenerated = perform(cantMove.tags.withAdditional("current action" to "regenerate"))
        assertThat(regenerated).lastPlot().isEqualTo("You regenerate to full health")
        assertThat(regenerated).tags()
            .containsAll(
                "posture" to "lying",
                "position" to "ground",
                "bones" to "okay",
            )
        val mobile = perform(regenerated.tags)
        assertThat(mobile).lastPlot().isEqualTo("You can move again")
        assertThat(mobile).tags()
            .containsAll(
                "mobile" to "true",
            )
        val finalResult = perform(mobile.tags.withAdditional("current action" to "stand"))

        assertThat(finalResult).lastPlot().isEqualTo("You get up")
        assertThat(finalResult).tags()
            .containsAll(
                "posture" to "standing",
                "position" to "ground",
                "bones" to "okay",
                "mobile" to "true",
            )
    }

    @Test
    fun `kek16`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
        )

        val flight = perform(tags.withAdditional("current action" to "learn flight"))
        assertThat(flight).tags()
            .contains("ability" to "flight")
        val immortality =
            perform(flight.tags.withAdditional("current action" to "learn immortality"))
        assertThat(immortality).tags()
            .contains("ability" to "[flight,immortality]")
        val regeneration = perform(
            immortality.tags.withAdditional("current action" to "learn regeneration")
        )

        assertThat(regeneration).tags()
            .contains("ability" to "[flight,immortality,regeneration]")
    }

    @Test
    fun `kek17`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "immortality" to "true",
            "ability" to "[flight,regeneration]",
        )
        val regenerated = perform(tags.withAdditional("current action" to "regenerate"))
        assertThat(regenerated).lastPlot().isEqualTo("You regenerate to full health")
        assertThat(regenerated).tags()
            .containsAll(
                "bones" to "okay",
            )
    }

    @Test
    fun `kek18`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "immortality" to "true",
            "ability" to "[flight,regeneration]",
        )
        val regenerated = perform(tags.withAdditional("current action" to "regenerate"))
        assertThat(regenerated).lastPlot().isEqualTo("You regenerate to full health")
        assertThat(regenerated).tags()
            .containsAll(
                "bones" to "okay",
                "mobile" to "true",
            )
    }

    @Test
    fun `should not be able to move when tied`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "tied" to "true",
        )
        val regenerated = perform(tags)
        assertThat(regenerated).lastPlot().isEqualTo("Now you can't move")
        assertThat(regenerated).tags()
            .containsAll(
                "tied" to "true",
                "mobile" to "false",
            )
    }

    @Test
    fun `should stop being tied when untie`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "tied" to "true",
        )
        val regenerated = perform(tags)
        val tied = perform(regenerated.tags.withAdditional("current action" to "untie"))
        assertThat(tied).lastPlot().isEqualTo("You free yourself")
        assertThat(tied).tags()
            .containsAll(
                "tied" to "false",
            )
    }

    @Test
    fun `should still be not mobile when regenerated while tied`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "tied" to "true",
            "bones" to "broken",
            "ability" to "regeneration"
        )
        val notMobile = perform(tags)
        val regenerated = perform(notMobile.tags.withAdditional("current action" to "regenerate"))
        assertThat(regenerated).lastPlot().isEqualTo("You regenerate to full health")
        assertThat(regenerated).tags()
            .containsAll(
                "bones" to "okay",
                "tied" to "true",
                "mobile" to "false",
            )
        val tied = perform(regenerated.tags.withAdditional("current action" to "stand"))
        assertThat(tied).lastPlot().isEqualTo("You can't get up")
        assertThat(tied).tags()
            .containsAll(
                "bones" to "okay",
                "tied" to "true",
                "mobile" to "false",
            )
    }

    @Test
    fun `should become mobile when regenerated while tied and then untied`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "tied" to "true",
            "bones" to "broken",
            "ability" to "regeneration"
        )
        val notMobile = perform(tags)
        val regenerated = perform(notMobile.tags.withAdditional("current action" to "regenerate"))
        val tied = perform(regenerated.tags.withAdditional("current action" to "stand"))
        val untied = newPerform(tied, action = "untie")
        assertThat(untied).lastPlotsNotReversed(2)
            .containsExactly("You free yourself", "You can move again")
        assertThat(untied).tags()
            .containsAll(
                "bones" to "okay",
                "tied" to "false",
                "mobile" to "true",
            )
    }

    @Test
    fun kek22() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
        )

        val kek = newPerform(tags.withAdditional("current action" to "fly"))

        assertThat(kek).lastPlot().isEqualTo("You start flying")
    }

    @Test
    fun kek23() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "appearance" to "human",
            "ability" to "flight",
            "posture" to "flying",
        )

        val kek = newPerform(tags.withAdditional("current action" to "stop flying"))

        assertThat(kek).plot()
            .containsExactly(
                "You stop flying",
                "You fall to the ground, breaking every bone in your body",
                "You have died",
                "Now you can't move",
            )
    }

    @Test
    fun kek24() {
        val tags = defaultTagsWithAdditional()

        val kek = newPerform(tags)

        assertThat(kek).plot()
            .isEmpty()
    }

    @Test
    fun kek25() {
        val tags = defaultTagsWithAdditional(
            "holding" to "knife",
            "sharp weapon" to "true",
        )

        val kek = newPerform(tags.withAdditional("current action" to "cut your hand"))

        assertThat(kek).all {
            tags().containsAll(
                "health" to "85",
                "bleeding" to "true",
            )
        }
    }

    @Test
    fun kek26() {
        val tags = defaultTagsWithAdditional(
            "holding" to "knife",
            "sharp weapon" to "true",
        )

        val cut1 = newPerform(tags.withAdditional("current action" to "cut your hand"))
        val cut2 = newPerform(cut1.tags.withAdditional("current action" to "cut your hand"))

        assertThat(cut2).all {
            tags().containsAll(
                "health" to "70",
                "bleeding" to "true",
            )
        }
    }

    @Test
    fun kek27() {
        val tags = defaultTagsWithAdditional(
            "objects" to "knife",
        )

        val kek = newPerform(tags.withAdditional("current action" to "pick up knife"))

        assertThat(kek).all {
            tags().containsAll(
                "holding" to "knife",
                "sharp weapon" to "true",
                "short weapon" to "true",
                "throwable weapon" to "true"
            )
        }
    }

    @Test
    fun `should stop if the only acceptable plot is the repeat of the previous one`() {
        val tags = defaultTagsWithAdditional(
            "posture" to "standing",
        )

        val kek = newPerform(tags.withAdditional("current action" to "stand"))

        assertThat(kek).plot().containsExactly("You are now standing")
    }

    @Test
    fun `should repeat plot if it is caused by action`() {
        val tags = defaultTagsWithAdditional(
            "posture" to "standing",
        )

        val kek = newPerform(tags.withAdditional("current action" to "stand"))
        val kek2 = newPerform(kek, action = "wait")
        val kek3 = newPerform(kek2, action = "wait")

        assertThat(kek3).plot()
            .containsExactly(
                "You are now standing",
                "You stand, doing nothing",
                "You stand, doing nothing",
            )
    }

    //TODO: I need to make this work somehow for both devourer and android without specifing tags
    @Test
    fun `when devourer hides in the forest they need meat`() {
        val lineEntry = line(
            requiredTags = spikeTags(),
            entry = entry(
                plot = "You hide in the forest for some time",
                tagChanges = spikeTags(tagKey("meat") to "\${-10}"),
            )
        )
        val tagsWithoutSupplies = spikeTags(tagKey("species") to tagValue("devourer"))
        val tagsWithSupplies = tagsWithoutSupplies.withAdditional("meat" to "10")

        assertThat(lineEntry.suitableFor(tagsWithoutSupplies)).isFalse()
        assertThat(lineEntry.suitableFor(tagsWithSupplies)).isTrue()
    }

    @Test
    fun `when android hides in the forest they need charge`() {
        val lineEntry = line(
            requiredTags = spikeTags(),
            entry = entry(
                plot = "You hide in the forest for some time",
                tagChanges = spikeTags(tagKey("charge") to "\${-100}"),
            )
        )
        val tagsWithoutSupplies = spikeTags(tagKey("species") to tagValue("android"))
        val tagsWithSupplies = tagsWithoutSupplies.withAdditional("charge" to "100")

        assertThat(lineEntry.suitableFor(tagsWithoutSupplies)).isFalse()
        assertThat(lineEntry.suitableFor(tagsWithSupplies)).isTrue()
    }

    private fun Assert<Game>.tags() = prop(Game::tags).strings()

    //TODO: Basically next refactoring is to teach different entities to have tags

    private fun Assert<Game>.plot() = prop(Game::plot)

    private fun Assert<Game>.lastPlot(): Assert<String> = plot().index(0)
    private fun Assert<Game>.lastPlotsNotReversed(n: Int): Assert<List<String>> =
        plot().transform { it.takeLast(n) }

    private fun Assert<Game>.lastPlotNotReversed(): Assert<String> = plot().transform { it.last() }

    private fun perform(tags: SpikeTags): Game {
        val game = game(
            tags = tags
        )
        return performm(game)
    }

    private fun newPerform(tags: SpikeTags): Game {
        return newPerform(game(tags = tags))
    }

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

fun Assert<SpikeTags>.strings() = transform {
    it.map { it.key.tagKey to it.value.value }
}
