package com.daniil.shevtsov.idle.feature.tagsystem.domain

typealias SpikeTags = Map<SpikeTagKey, SpikeTag>
typealias SpikeTagValue = String
private typealias Plot = String

data class SpikeTag(
    val key: SpikeTagKey,
    val value: SpikeTagValue,
)

fun spikeTag(
    key: SpikeTagKey = tagKey(key = ""),
    value: SpikeTagValue = "",
) = SpikeTag(
    key = key,
    value = value,
)

data class SpikeTagKey(
    val tagKey: String,
    val entityId: String? = null,
)

fun tagKey(key: String, entityId: String? = null) = SpikeTagKey(
    tagKey = key,
    entityId = entityId,
)

fun tagValue(raw: String = ""): SpikeTagValue = raw

fun createDefaultTags(): Map<SpikeTagKey, SpikeTag> = tags(
    "mobile" to "true",
    "health" to "100",
    "life" to "alive",
)

fun spikeTags(vararg entries: Pair<SpikeTagKey, SpikeTagValue>): SpikeTags =
    entries.associate { (key, value) ->
        key to spikeTag(key = key, value = value)
    }

fun List<Pair<String, String>>.toSpikeTags() = associate { (key, value) ->
    val tagKey = tagKey(key = key)
    tagKey to spikeTag(key = tagKey, value = value)
}

fun defaultTagsWithAdditional(vararg tags: Pair<String, String>): SpikeTags =
    createDefaultTags().withAdditional(*tags)

fun SpikeTags.withAdditional(vararg tags: Pair<String, String>): SpikeTags =
    this + tags.toList().toSpikeTags()

fun tags(
    vararg entries: Pair<String, String>
): SpikeTags = entries.toList().toSpikeTags()

fun entry(
    plot: String,
    tagChange: SpikeTags = mapOf(),
    weight: Float = 1.0f
): LineEntry =
    LineEntry(
        tags = tagChange,
        plot = plot,
        weight = weight,
    )

data class LineEntry(
    val tags: SpikeTags,
    val plot: Plot,
    val weight: Float,
)

data class PerformResult(
    val tags: SpikeTags,
    val plot: List<Plot>,
)

data class Line(
    val requiredTags: SpikeTags,
    val entry: LineEntry,
)

fun performm(tags: SpikeTags): Game {
    val game = game(
        tags = tags
    )
    return performm(game)
}

fun performm(game: Game, action: String? = null): Game {
    return update(
        game,
        action = action ?: game.tags[tagKey("current action")]?.value ?: "default"
    )
}

fun newPerform(tags: SpikeTags): Game {
    var kek = performm(tags)
    var count = 0
    while (!kek.plot.contains("no suitable entry") && count < 20) {
        val lol = performm(kek.tags)
        kek = lol.copy(
            plot = kek.plot + lol.plot
        )
        count++
    }
    return kek.copy(
        plot = kek.plot.filter { it != "no suitable entry" }
    )
}

fun SpikeTags.containsKey(key: String) = any { (tagKey, _) -> tagKey.tagKey == key }

private fun perform(tags: SpikeTags): PerformResult {
    val lines = lines.map { line ->
        when (line.requiredTags.containsKey("current action")) {
            true -> line.copy(
                entry = line.entry.copy(
                    weight = line.entry.weight + 100f
                )
            )

            false -> line
        }
    }

    val sortedEntries = lines
        .filter { line ->
            line.requiredTags == listOf("" to "") || line.requiredTags.all { (requiredTag, requiredTagValue) ->
                val currentTags = tags
                val currentValue = currentTags[requiredTag]

                val currentValues = when {
                    currentValue == null -> emptyList()
                    currentValue.value.contains('[') -> currentValue.value
                        .substringAfter('[')
                        .substringBefore(']')
                        .split(',')

                    else -> listOf(currentValue.value)
                }
                when {
                    requiredTagValue.value.contains('!') -> !currentValues.contains(
                        requiredTagValue.value.drop(1)
                    )

                    else -> currentValues.contains(requiredTagValue.value)
                }
            }
        }
        .sortedWith(
            compareBy(
                { (_, entry) -> entry.weight },
                { (lineTags, _) ->
                    lineTags.count { (key, value) -> tags[key] == value }
                }
            )
        ).reversed()

    val mostSuitableEntry = sortedEntries.firstOrNull()?.entry ?: entry("no suitable entry")
    val mostSuitableLine = mostSuitableEntry.plot

    val modifiedTags = tags.toMutableMap().apply {
        remove(tagKey(key = "current action"))

        mostSuitableEntry.tags.forEach { (tag, valueToAdd) ->
            val oldValue = get(tag)
            val newValue = when {
                valueToAdd.value.contains('+') && oldValue != null -> {
                    val oldValueWithoutBrackets =
                        oldValue.value.substringAfter('[').substringBefore(']')
                    val valueToAddWithoutBrackets =
                        valueToAdd.value.substringAfter('[').substringBefore(']')
                    "[$oldValueWithoutBrackets,$valueToAddWithoutBrackets]"
                }

                valueToAdd.value.contains("\${-") && oldValue != null -> {
                    val oldValueNumber = oldValue.value.toInt()
                    val valueToAddNumber =
                        valueToAdd.value.substringAfter('{').substringBefore('}').toInt()
                    (oldValueNumber + valueToAddNumber).toString()
                }

                else -> valueToAdd.value
            }
            put(tag, valueToAdd.copy(value = newValue))
        }
    }

    return PerformResult(
        tags = modifiedTags,
        plot = listOf(mostSuitableLine),
    )
}

data class Location(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Container(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Player(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Item(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Npc(override val id: String, val title: String, override val tags: SpikeTags) : TagHolder
data class DialogLine(
    override val id: String,
    val text: String,
    override val requiredTags: SpikeTags,
    override val tagChanges: SpikeTags,
) : TagRequirer, TagChanger

fun dialogLine(
    id: String = "",
    text: String = "",
    requiredTags: SpikeTags = tags(),
    tagChanges: SpikeTags = tags(),
) =
    DialogLine(id = id, text = text, requiredTags = requiredTags, tagChanges = tagChanges)

data class Game(
    override val id: String,
    val dialogLines: List<DialogLine>,
    val locations: List<Location>,
    val locationId: String,
    val player: Player,
    val npcs: List<Npc>,
    override val tags: SpikeTags,
    val plot: List<String>,
) : TagHolder

fun update(game: Game, action: String): Game {

    val newLocationTags: SpikeTags =
        game.locations.find { it.id == game.locationId }?.let { location ->
            location.tags.map { (_, tag) ->
                tagKey(
                    key = tag.key.tagKey,
                    entityId = location.id
                ) to tag
            } + listOf(tagKey("location") to spikeTag(tagKey("location"), tagValue(location.id)))
        }.orEmpty().toMap()
    val newPlayerTags: SpikeTags = game.player.tags.map { (_, tag) ->
        tagKey(key = tag.key.tagKey, entityId = "player") to tag
    }.toMap()
    val newNpcTags: SpikeTags = game.npcs.flatMap { npc ->
        npc.tags.map { (_, tag) ->
            tagKey(key = tag.key.tagKey, entityId = npc.id) to tag
        }
    }.toMap()
    val newGameTags = game.tags

    val newTags: SpikeTags = (newLocationTags + newPlayerTags + newNpcTags) + newGameTags + mapOf(
        tagKey("current action") to spikeTag(tagKey("current action"), tagValue(action))
    )

    val performResult = when (action) {
        "speak" -> {
            val npcToSpeak = game.npcs.first()
            val npcName = npcToSpeak.tags.getTagValue(tagKey("name"))?.value
            val npcOccupation = npcToSpeak.tags.getTagValue(tagKey("occupation"))?.value
            val speakerIndication = "$npcName ($npcOccupation):"

            val filteredDialogLines = game.dialogLines.filter { dialogLine ->
                dialogLine.requiredTags.all { requiredTag ->
                    val hasKey = newTags.containsTagKey(requiredTag.key)
                    val tagValue = newTags.getTagValue(requiredTag.key)
                    val hasValue = tagValue == requiredTag.value

                    hasKey && hasValue
                }
            }
            val sortedDialogLines = filteredDialogLines.sortedWith(compareBy {
                it.requiredTags.count { requiredTag ->
                    val hasKey = newTags.containsTagKey(requiredTag.key)
                    val tagValue = newTags.getTagValue(requiredTag.key)
                    val hasValue = tagValue == requiredTag.value

                    hasKey && hasValue
                }
            })
            val dialogLine = sortedDialogLines.lastOrNull()

            val selectedLine = when {
                newTags.containsTagKeys(
                    tagKey(key = "location:saloon", entityId = "location"),
                    tagKey(key = "dialog:greetings", entityId = "dialog"),
                ) -> "$speakerIndication ${dialogLine?.text}"

                newTags.getTagValue(tagKey("location"))?.value == tagValue("saloon") -> "$speakerIndication ${dialogLine?.text}"
                else -> null
            }

            PerformResult(
                tags = newTags,
                plot = game.plot + selectedLine?.let { listOf(selectedLine) }.orEmpty()
            )
        }

        else -> perform(newTags)
    }

    return game.copy(
        tags = performResult.tags,
        plot = performResult.plot,
    )
}

private fun SpikeTags.getTagValue(key: SpikeTagKey) = (entries.find { (tagKey, tag) ->
    tagKey == key
} ?: entries.find { (tagKey, tag) ->
    tagKey.tagKey.contains(key.tagKey)
})?.value

private fun SpikeTags.containsTagKeys(vararg keys: SpikeTagKey) = keys.all { key ->
    any { (tagKey, tag) -> tagKey.tagKey.contains(key.tagKey) }
}

private fun SpikeTags.containsTagKey(key: SpikeTagKey) =
    any { (tagKey, tag) -> tagKey.tagKey.contains(key.tagKey) }

fun game(
    id: String = "game",
    locations: List<Location> = emptyList(),
    locationId: String = "",
    player: Player = player(),
    dialogLines: List<DialogLine> = emptyList(),
    npcs: List<Npc> = emptyList(),
    tags: SpikeTags = tags(),
    plot: List<String> = emptyList(),
) = Game(
    id = id,
    locationId = locationId,
    locations = locations,
    dialogLines = dialogLines,
    player = player,
    npcs = npcs,
    tags = tags,
    plot = plot,
)

fun location(id: String = "location", title: String = "", tags: SpikeTags = tags()) = Location(
    id = id,
    title = title,
    tags = tags,
)

fun container(id: String = "container", title: String = "", tags: SpikeTags = tags()) = Container(
    id = id,
    title = title,
    tags = tags,
)

fun player(id: String = "player", title: String = "", tags: SpikeTags = tags()) = Player(
    id = id,
    title = title,
    tags = tags,
)

fun item(id: String = "item", title: String = "", tags: SpikeTags = tags()) = Item(
    id = id,
    title = title,
    tags = tags,
)

fun npc(id: String = "npc", title: String = "", tags: SpikeTags = tags()) = Npc(
    id = id,
    title = title,
    tags = tags,
)

interface TagHolder {
    val id: String
    val tags: SpikeTags
}

interface TagRequirer {
    val id: String
    val requiredTags: SpikeTags
}

interface TagChanger {
    val id: String
    val tagChanges: SpikeTags
}
