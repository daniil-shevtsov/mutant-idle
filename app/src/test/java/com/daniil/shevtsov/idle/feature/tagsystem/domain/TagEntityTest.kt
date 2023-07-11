package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.prop
import org.junit.jupiter.api.Test

class TagEntityTest {

    @Test
    fun `should add player and location tags`() {
        val game = game(
            player = player(tags = tags("name" to "bob")),
            locations = listOf(location(id = "saloon", tags = tags("SpaceType" to "indoors"))),
            locationId = "saloon",
        )
        val updated = update(game, "init")
        assertThat(updated)
            .tags()
            .containsAll(
                "player:name" to "bob",
                "location:saloon:SpaceType" to "indoors",
            )
    }

    private fun Assert<TagHolder>.tags() = prop(TagHolder::tags)

}
