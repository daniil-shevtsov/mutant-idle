package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.index
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.defaultTagsWithAdditional
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.withAdditional
import org.junit.jupiter.api.Test

class TagSystemNewPerformTest {

    @Test
    fun `should start flying`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "appearance" to "human",
            "ability" to "flight",
        )

        val game = newPerform(tags.withAdditional("current action" to "fly"))

        assertThat(game).lastPlot().isEqualTo("You start flying")
    }

    @Test
    fun `should add all plots when stopped flying`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "appearance" to "human",
            "ability" to "flight",
            "posture" to "flying",
        )

        val game = newPerform(tags.withAdditional("current action" to "stop flying"))

        assertThat(game).plot()
            .containsExactly(
                "You stop flying",
                "You fall to the ground, breaking every bone in your body",
                "You have died",
                "Now you can't move",
            )
    }

    @Test
    fun `should have no plot initially`() {
        val tags = defaultTagsWithAdditional()

        val game = newPerform(tags)

        assertThat(game).plot()
            .isEmpty()
    }

    @Test
    fun `should lower health and start bleeding when cutting your hand`() {
        val tags = defaultTagsWithAdditional(
            "holding" to "knife",
            "sharp weapon" to "true",
        )

        val game = newPerform(tags.withAdditional("current action" to "cut your hand"))

        assertThat(game).all {
            tags().containsAll(
                "health" to "85",
                "bleeding" to "true",
            )
        }
    }

    @Test
    fun `should lower health further when cutting hand twice`() {
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
    fun `should hold knife and get its tags when picking up knife`() {
        val tags = defaultTagsWithAdditional(
            "objects" to "knife",
        )

        val game = newPerform(tags.withAdditional("current action" to "pick up knife"))

        assertThat(game).all {
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

        val game = newPerform(tags.withAdditional("current action" to "stand"))

        assertThat(game).plot().containsExactly("You are now standing")
    }

    @Test
    fun `should repeat plot if it is caused by action`() {
        val tags = defaultTagsWithAdditional(
            "posture" to "standing",
        )

        val game = newPerform(tags.withAdditional("current action" to "stand"))
        val game2 = newPerform(game, action = "wait")
        val game3 = newPerform(game2, action = "wait")

        assertThat(game3).plot()
            .containsExactly(
                "You are now standing",
                "You stand, doing nothing",
                "You stand, doing nothing",
            )
    }

    private fun newPerform(tags: SpikeTags): Game {
        return newPerform(game(tags = tags))
    }

}

fun Assert<Game>.tags() = prop(Game::tags).strings()

fun Assert<Game>.lastPlotsNotReversed(n: Int): Assert<List<String>> =
    plot().transform { it.takeLast(n) }

fun Assert<SpikeTags>.strings() = transform {
    it.map { it.key.tagKey to it.value.value }
}

fun Assert<Game>.plot() = prop(Game::plot)

fun Assert<Game>.lastPlot(): Assert<String> = plot().index(0)
