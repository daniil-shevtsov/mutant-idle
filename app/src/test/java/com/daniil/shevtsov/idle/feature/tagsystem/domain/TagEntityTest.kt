package com.daniil.shevtsov.idle.feature.tagsystem.domain

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.assertions.prop
import assertk.assertions.support.expected
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.dialogLine
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.withAdditional
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

    @Test
    fun `should get sword tags when picked up sword`() {
        val game = game(
            player = player(
                tags = tags(
                    "name" to "bob",
                    "location" to "saloon",
                )
            ),
            lines = listOf(),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
            items = listOf(
                item(
                    id = "sword", title = "sword", tags = spikeTags(
                        tagKey("weapon type") to tagValue("sharp"),
                        tagKey("weapon length") to tagValue("long"),
                    )
                ),
            ),
            tags = spikeTags(),
        )

        val kek = update(game, "pick up sword")

        assertThat(kek).all {
            prop(TagHolder::tags).containsTags(
                tagKey("holding") to tagValue("sword"),
                tagKey("weapon type", entityId = "sword") to tagValue("sharp"),
                tagKey("weapon length", entityId = "sword") to tagValue("long"),
            )
        }
    }

    @Test
    fun `should get spear tags when picked up spear`() {
        val game = game(
            player = player(
                tags = tags(
                    "name" to "bob",
                    "location" to "saloon",
                )
            ),
            lines = listOf(),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
            items = listOf(
                item(
                    id = "spear", title = "spear", tags = spikeTags(
                        tagKey("weapon type", entityId = "spear") to tagValue("piercing"),
                        tagKey("weapon length", entityId = "spear") to tagValue("long"),
                        tagKey("throwable", entityId = "spear") to tagValue("true"),
                    )
                ),
            ),
            tags = spikeTags(),
        )

        val kek = update(game, "pick up spear")

        assertThat(kek).all {
            prop(TagHolder::tags).containsTags(
                tagKey("holding") to tagValue("spear"),
                tagKey("weapon type", entityId = "spear") to tagValue("piercing"),
                tagKey("weapon length", entityId = "spear") to tagValue("long"),
                tagKey("throwable", entityId = "spear") to tagValue("true"),
            )
            lastPlot().isEqualTo("You pick up spear")
        }
    }

    @Test
    @Disabled
    fun `should throw held item at target when throwable`() {
        val game = game(
            player = player(
                tags = tags(
                    "name" to "bob",
                    "location" to "saloon",
                )
            ),
            lines = listOf(),
            locations = listOf(
                location(
                    id = "saloon",
                    tags = tags("SpaceType" to "indoors")
                )
            ),
            locationId = "saloon",
            items = listOf(
                item(
                    id = "spear", title = "spear", tags = spikeTags(
                        tagKey("weapon type", entityId = "spear") to tagValue("piercing"),
                        tagKey("weapon length", entityId = "spear") to tagValue("long"),
                        tagKey("throwable", entityId = "spear") to tagValue("true"),
                    )
                ),
            ),
            tags = spikeTags(
                tagKey("holding") to tagValue("spear"),
                tagKey("weapon type", entityId = "spear") to tagValue("piercing"),
                tagKey("weapon length", entityId = "spear") to tagValue("long"),
                tagKey("throwable", entityId = "spear") to tagValue("true"),
            ),
        )

        val kek = update(game, "throw")

        assertThat(kek).all {
            prop(TagHolder::tags)
                .all {
                    containsTags(
                        tagKey("holding") to tagValue("null"),
                        tagKey("weapon type", entityId = "spear") to tagValue("piercing"),
                        tagKey("weapon length", entityId = "spear") to tagValue("long"),
                        tagKey("throwable", entityId = "spear") to tagValue("true"),
                    )
                }
            //lastPlot().isEqualTo("you throw ")
        }
    }

    private fun Assert<SpikeTags>.containsTags(
        vararg tags: Pair<SpikeTagKey, SpikeTagValue>
    ) = given { actual ->
        val tagAssertResult = verifyContainsTags(
            expected = tags.toMap(),
            actual = actual.map { it.key to it.value.value }.toMap(),
        )

        when (tagAssertResult) {
            is TagAssertResult.Pass -> return@given
            is TagAssertResult.Fail -> expected(tagAssertResult.message)
        }
    }

    private fun Assert<SpikeTags>.containsNoTags(
        vararg tags: Pair<SpikeTagKey, SpikeTagValue>
    ) = given { actual ->
        val tagAssertResult = verifyContainsTags(
            expected = tags.toMap(),
            actual = actual.map { it.key to it.value.value }.toMap(),
        )

        when (tagAssertResult) {
            is TagAssertResult.Pass -> return@given
            is TagAssertResult.Fail -> expected(tagAssertResult.message)
        }
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

    private fun Assert<TagHolder>.tags(): Assert<List<Pair<SpikeTagKey, SpikeTagValue>>> =
        prop(TagHolder::tags)
            .transform { it.map { it.key to it.value.value } }

    private fun Assert<TagHolder>.tagStrings(): Assert<List<Pair<String, String>>> =
        tags().transform { it.map { it.first.tagKey to it.second } }

}

sealed interface TagAssertResult {
    object Pass : TagAssertResult
    data class Fail(val message: String) : TagAssertResult
}

fun verifyContainsTags(
    expected: Map<SpikeTagKey, SpikeTagValue>,
    actual: Map<SpikeTagKey, SpikeTagValue>,
): TagAssertResult {
    val notFoundPairs = expected.filter { expectedEntry ->
        actual[expectedEntry.key] != expectedEntry.value
    }
    if (notFoundPairs.isEmpty()) {
        return TagAssertResult.Pass
    }
    return TagAssertResult.Fail(
        message = "to contain:\n${
            expected.toMessage()
        }\n" +
                "but\n" + notFoundPairs.toList()
            .joinToString(separator = "\n") { tagPair ->
                val (expectedKey, expectedValue) = tagPair
                when {
                    !actual.containsKey(expectedKey) -> "no tag with ${expectedKey.toMessage()}"

                    actual[expectedKey] != expectedValue -> "${expectedKey.toMessage()} tag's value is ${actual[expectedKey]} instead of $expectedValue"

                    else -> tagPair.toMessage()
                }

            } + "\nactual:\n" + when {
            actual.isNotEmpty() -> actual.toMessage()

            else -> "tags are empty"
        }
                + "\n\n"
    )
}

fun verifyContainsNoTags(
    expected: Map<SpikeTagKey, SpikeTagValue>,
    actual: Map<SpikeTagKey, SpikeTagValue>,
): TagAssertResult {
    val presentTags = expected.filter { expectedEntry ->
        actual[expectedEntry.key] == expectedEntry.value
    }
    if (presentTags.isEmpty()) {
        return TagAssertResult.Pass
    }
    return TagAssertResult.Fail(
        """to not contain:
                |${expected.toMessage()}
                |but present:
                |${presentTags.toMessage()}
                |actual:
                |${actual.toMessage()}
                |
                |""".trimMargin()
    )
}

private fun Map<SpikeTagKey, SpikeTagValue>.toMessage() = toList()
    .joinToString(separator = "\n") { tagPair ->
        tagPair.toMessage()
    }

private fun Pair<SpikeTagKey, SpikeTagValue>.toMessage(): String {
    val (expectedKey, expectedValue) = this
    return "(${tagKeyMessage(expectedKey)}, value=${expectedValue})"
}

private fun SpikeTagKey.toMessage() =
    "(${tagKeyMessage(this)})"

private fun tagKeyMessage(tagKey: SpikeTagKey) =
    tagKey.entityId?.let { "entity=${tagKey.entityId}, " }.orEmpty() + "key=${tagKey.tagKey}"
