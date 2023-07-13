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
                tagKey(entityId = "player", key = "player:name") to tagValue("bob"),
                tagKey(
                    entityId = "saloon",
                    key = "location:saloon:SpaceType"
                ) to tagValue("indoors"),
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
                        tagKey(entityId = "player", key = "player:name") to tagValue("bob"),
                        tagKey(entityId = "Bill", key = "npc:Bill:name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "npc:Bill:occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "location:saloon:SpaceType"
                        ) to tagValue("indoors"),
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
                    tagChanges = tags("dialog:drink_offered" to "true"),
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
                        tagKey(entityId = "player", key = "player:name") to tagValue("bob"),
                        tagKey(entityId = "Bill", key = "npc:Bill:name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "npc:Bill:occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "location:saloon:SpaceType"
                        ) to tagValue("indoors"),
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
                    requiredTags = tags("dialog:drink_offered" to "true"),
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
                        tagKey(entityId = "player", key = "player:name") to tagValue("bob"),
                        tagKey(entityId = "player", key = "player:money") to tagValue("90"),
                        tagKey(entityId = "player", key = "player:holding") to tagValue("beer"),
                        tagKey(entityId = "Bill", key = "npc:Bill:name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "npc:Bill:occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "location:saloon:SpaceType"
                        ) to tagValue("indoors"),
                    )
                plot()
                    .containsExactly("Bill (barkeep): Here is your drink.")
            }
    }

    private fun Assert<TagHolder>.tags(): Assert<List<Pair<SpikeTagKey, SpikeTagValue>>> =
        prop(TagHolder::tags)
            .transform { it.map { it.key to it.value.value } }

    private fun Assert<TagHolder>.tagStrings(): Assert<List<Pair<String, String>>> =
        tags().transform { it.map { it.first.tagKey to it.second } }

    private fun Assert<Game>.plot() = prop(Game::plot)

}
