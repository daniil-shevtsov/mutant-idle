package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.daniil.shevtsov.idle.feature.action.domain.action
import org.junit.jupiter.api.Test

internal class TagSystemTest {
    private val tagSystem = TagSystem()

    @Test
    fun `should make action available if player has required tags`() {
        val devourerTag = tag(name = "Devourer")
        val corpsesTag = tag(name = "Fresh corpses access")

        val playerTags = listOf(
            devourerTag,
            corpsesTag,
        )

        val action = action(id = 0L,
            title = "Eat organ",
            tags = mapOf(
                devourerTag to TagRelation.Required,
                corpsesTag to TagRelation.Required,
            ))

        val isActionAvailable = tagSystem.isActionAvailable(
            playerTags = playerTags,
            action = action
        )

        assertThat(isActionAvailable).isTrue()
    }

    @Test
    fun `should make action not available if player does not have required tags`() {
        val devourerTag = tag(name = "Devourer")
        val corpsesTag = tag(name = "Fresh corpses access")

        val playerTags = listOf(
            devourerTag,
        )

        val action = action(id = 0L,
            title = "Eat organ",
            tags = mapOf(
                devourerTag to TagRelation.Required,
                corpsesTag to TagRelation.Required,
            ))

        val isActionAvailable = tagSystem.isActionAvailable(
            playerTags = playerTags,
            action = action
        )

        assertThat(isActionAvailable).isFalse()
    }

}
