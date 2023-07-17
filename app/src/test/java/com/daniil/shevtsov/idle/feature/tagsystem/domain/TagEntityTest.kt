package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.dialogLine
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tags
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
                tagKey(entityId = "player", key = "name") to tagValue("bob"),
                tagKey(
                    entityId = "saloon",
                    key = "SpaceType"
                ) to tagValue("indoors"),
            )
    }

    @Test
    fun `should say howdy when inside saloon`() {
        val game = game(
            player = player(tags = tags("name" to "bob", "location" to "saloon")),
            lines = listOf(
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
                        tagKey(entityId = "player", key = "name") to tagValue("bob"),
                        tagKey(entityId = "Bill", key = "name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "SpaceType"
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
            lines = listOf(
                dialogLine(
                    id = "1234",
                    text = "Howdy!",
                    requiredTags = tags("location" to "saloon")
                ),
                dialogLine(id = "456", text = "Hello!"),
                dialogLine(
                    id = "789",
                    text = "Would you like a drink?",
                    requiredTags = spikeTags(
                        tagKey("location") to tagValue("saloon"),
                        tagKey(entityId = "dialog", key = "greetings") to tagValue("true"),
                    ),
                    tagChanges = spikeTags(
                        tagKey(
                            entityId = "dialog",
                            key = "drink_offered"
                        ) to tagValue("true")
                    ),
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
            tags = spikeTags(tagKey(entityId = "dialog", key = "greetings") to tagValue("true")),
        )
        val updated = update(game, "speak")
        assertThat(updated)
            .all {
                tags()
                    .containsAll(
                        tagKey(entityId = "player", key = "name") to tagValue("bob"),
                        tagKey(entityId = "Bill", key = "name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "SpaceType"
                        ) to tagValue("indoors"),
                    )
                plot()
                    .containsExactly("Bill (barkeep): Would you like a drink?")
            }
    }

    @Test
    fun `should accept the drink after being offered when barkeep inside saloon`() {
        val game = game(
            player = player(
                tags = tags(
                    "name" to "bob",
                    "location" to "saloon",
                    "money" to "100",
                )
            ),
            lines = listOf(
                dialogLine(
                    id = "1234",
                    text = "Howdy!",
                    requiredTags = tags("location" to "saloon")
                ),
                dialogLine(id = "456", text = "Hello!"),
                dialogLine(
                    id = "789",
                    text = "Here is your drink.",
                    requiredTags = spikeTags(
                        tagKey(
                            entityId = "dialog",
                            key = "drink_offered"
                        ) to tagValue("true")
                    ),
                    tagChanges = spikeTags(
                        tagKey(
                            entityId = "player",
                            key = "money"
                        ) to tagValue("\${-10}"), tagKey(
                            entityId = "player",
                            key = "holding"
                        ) to tagValue("beer")
                    )
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
            tags = spikeTags(
                tagKey(
                    entityId = "dialog",
                    key = "drink_offered"
                ) to tagValue("true")
            ),
        )
        val updated = update(game, "speak")
        assertThat(updated)
            .all {
                plot()
                    .containsExactly("Bill (barkeep): Here is your drink.")
                tags()
                    .containsAll(
                        tagKey(entityId = "player", key = "name") to tagValue("bob"),
                        tagKey(entityId = "player", key = "money") to tagValue("90"),
                        tagKey(entityId = "player", key = "holding") to tagValue("beer"),
                        tagKey(entityId = "Bill", key = "name") to tagValue("Bill"),
                        tagKey(
                            entityId = "Bill",
                            key = "occupation"
                        ) to tagValue("barkeep"),
                        tagKey(
                            entityId = "saloon",
                            key = "SpaceType"
                        ) to tagValue("indoors"),
                    )
            }
    }

    private fun Assert<TagHolder>.tags(): Assert<List<Pair<SpikeTagKey, SpikeTagValue>>> =
        prop(TagHolder::tags)
            .transform { it.map { it.key to it.value.value } }

    private fun Assert<TagHolder>.tagStrings(): Assert<List<Pair<String, String>>> =
        tags().transform { it.map { it.first.tagKey to it.second } }

    private fun Assert<Game>.plot() = prop(Game::plot)

}
