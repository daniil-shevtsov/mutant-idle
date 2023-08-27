package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.defaultTagsWithAdditional
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.withAdditional
import org.junit.jupiter.api.Test

class TagSystemOldPerformTest {
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
    fun `should lie on the ground`() {
        val tags = defaultTagsWithAdditional(
            "position" to "ground",
            "posture" to "lying",
            "appearance" to "human",
        )

        val line = perform(tags.withAdditional("current action" to "wait"))

        assertThat(line).lastPlot().isEqualTo("You lie on the ground, doing nothing")
    }

    @Test
    fun `should fall to the ground when in the air`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "appearance" to "human",
        )

        val line = perform(tags)

        assertThat(line).lastPlot()
            .isEqualTo("You fall to the ground, breaking every bone in your body")
    }

    @Test
    fun `should fly when in the air while flying`() {
        val tags = defaultTagsWithAdditional(
            "position" to "low-air",
            "posture" to "flying",
            "appearance" to "human",
        )

        val line = perform(tags.withAdditional("current action" to "wait"))

        assertThat(line).lastPlot().isEqualTo("You fly in low-air")
    }

    @Test
    fun `should start flying when fly on the ground`() {
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
    fun `should keep all health when fell to the ground while indestructible`() {
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
    fun `should loose health when fell to the ground`() {
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
    fun `should die when no health`() {
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
    fun `should be immobile when all bones are broken`() {
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
    fun `should get full health and mobility when regenerated`() {
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
    fun `should learn abilities`() {
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
    fun `should heal bones when regenerating`() {
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
    fun `should become mobile when regenerated`() {
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

    private fun perform(tags: SpikeTags): Game {
        val game = game(
            tags = tags
        )
        return performm(game)
    }
}
