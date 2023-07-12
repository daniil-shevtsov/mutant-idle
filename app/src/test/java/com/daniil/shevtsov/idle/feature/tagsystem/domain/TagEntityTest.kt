package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.prop
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class TagEntityTest {

    @Test
    fun `should add player and location tags to the pile`() {
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
    //TODO: Need general filtering behavior and general update behavior

    @Test
    fun `should say howdy when inside saloon`() {
        val game = game(
            player = player(tags = tags("name" to "bob", "location" to "saloon")),
            dialogLines = listOf(
                dialogLine(
                    id = "1234",
                    text = "Howdy!",
                    requiredTags = tags("location" to "saloon")
                ),
                dialogLine(id = "456", text = "Hello!"),
            ),
            npcs = listOf(
                npc(
                    id = "Bill",
                    tags = tags("occupation" to "barkeep", "name" to "Bill", "location" to "saloon")
                )
            ),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
        )
        val updated = update(game, "speak")
        assertThat(updated)
            .all {
                tags()
                    .containsAll(
                        "player:name" to "bob",
                        "npc:Bill:name" to "Bill",
                        "npc:Bill:occupation" to "barkeep",
                        "location:saloon:SpaceType" to "indoors",
                    )
                plot()
                    .containsExactly("Bill (barkeep): Howdy!")
            }
    }

    @Test
    fun `should offer drink after greeting when barkeep inside saloon`() {
        val game = game(
            player = player(tags = tags("name" to "bob", "location" to "saloon")),
            dialogLines = listOf(
                dialogLine(
                    id = "1234",
                    text = "Howdy!",
                    requiredTags = tags("location" to "saloon")
                ),
                dialogLine(id = "456", text = "Hello!"),
                dialogLine(
                    id = "789",
                    text = "Would you like a drink?",
                    requiredTags = tags("location" to "saloon", "dialog:greetings" to "true"),
                ),
            ),
            npcs = listOf(
                npc(
                    id = "Bill",
                    tags = tags("occupation" to "barkeep", "name" to "Bill", "location" to "saloon")
                )
            ),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
            tags = tags("dialog:greetings" to "true"),
        )
        val updated = update(game, "speak")
        assertThat(updated)
            .all {
                tags()
                    .containsAll(
                        "player:name" to "bob",
                        "npc:Bill:name" to "Bill",
                        "npc:Bill:occupation" to "barkeep",
                        "location:saloon:SpaceType" to "indoors",
                    )
                plot()
                    .containsExactly("Bill (barkeep): Would you like a drink?")
            }
    }

    @Test
    @Disabled
    fun `should accept the drink after being offered when barkeep inside saloon`() {
        val game = game(
            player = player(
                tags = tags(
                    "name" to "bob",
                    "location" to "saloon",
                    "money" to "100",
                )
            ),
            dialogLines = listOf(
                dialogLine(
                    id = "1234",
                    text = "Howdy!",
                    requiredTags = tags("location" to "saloon")
                ),
                dialogLine(id = "456", text = "Hello!"),
                dialogLine(
                    id = "789",
                    text = "Here is your drink.",
                    tagChanges = tags("player:money" to "-10", "player:holding" to "beer")
                ),
            ),
            npcs = listOf(
                npc(
                    id = "Bill",
                    tags = tags("occupation" to "barkeep", "name" to "Bill", "location" to "saloon")
                )
            ),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
            tags = tags("dialog:drink_offered" to "true"),
        )
        val updated = update(game, "speak")
        assertThat(updated)
            .all {
                tags()
                    .containsAll(
                        "player:name" to "bob",
                        "player:money" to "90",
                        "player:holding" to "beer",
                        "npc:Bill:name" to "Bill",
                        "npc:Bill:occupation" to "barkeep",
                        "location:saloon:SpaceType" to "indoors",
                    )
                plot()
                    .containsExactly("Bill (barkeep): Here is your drink.")
            }
    }

    private fun Assert<TagHolder>.tags() = prop(TagHolder::tags)
    private fun Assert<Game>.plot() = prop(Game::plot)

}
