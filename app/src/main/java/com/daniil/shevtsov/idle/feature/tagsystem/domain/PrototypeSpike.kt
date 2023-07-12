package com.daniil.shevtsov.idle.feature.tagsystem.domain

typealias SpikeTags = Map<String, String>
typealias SpikeTag = Pair<String, String>
private typealias Plot = String


fun createDefaultTags() = mapOf(
    "mobile" to "true",
    "health" to "100",
    "life" to "alive",
)

fun defaultTagsWithAdditional(vararg tags: SpikeTag): SpikeTags =
    createDefaultTags().withAdditional(*tags)

fun SpikeTags.withAdditional(vararg tags: SpikeTag): SpikeTags =
    this + tags.toMap()

fun tags(vararg entries: SpikeTag): SpikeTags =
    entries.toList().toMap()

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


fun newPerform(tags: SpikeTags): PerformResult {
    var kek = perform(tags)
    var count = 0
    while (!kek.plot.contains("no suitable entry") && count < 20) {
        val lol = perform(kek.tags)
        kek = lol.copy(
            plot = kek.plot + lol.plot
        )
        count++
    }
    return kek.copy(
        plot = kek.plot.filter { it != "no suitable entry" }
    )
}

fun perform(tags: SpikeTags): PerformResult {
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
                    currentValue.contains('[') -> currentValue
                        .substringAfter('[')
                        .substringBefore(']')
                        .split(',')

                    else -> listOf(currentValue)
                }
                when {
                    requiredTagValue.contains('!') -> !currentValues.contains(
                        requiredTagValue.drop(
                            1
                        )
                    )

                    else -> currentValues.contains(requiredTagValue)
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
        remove("current action")

        mostSuitableEntry.tags.forEach { (tag, valueToAdd) ->
            val oldValue = get(tag)
            val newValue = when {
                valueToAdd.contains('+') && oldValue != null -> {
                    val oldValueWithoutBrackets = oldValue.substringAfter('[').substringBefore(']')
                    val valueToAddWithoutBrackets =
                        valueToAdd.substringAfter('[').substringBefore(']')
                    "[$oldValueWithoutBrackets,$valueToAddWithoutBrackets]"
                }

                valueToAdd.contains("\${-") && oldValue != null -> {
                    val oldValueNumber = oldValue.toInt()
                    val valueToAddNumber =
                        valueToAdd.substringAfter('{').substringBefore('}').toInt()
                    (oldValueNumber + valueToAddNumber).toString()
                }

                else -> valueToAdd
            }
            put(tag, newValue)
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

data class Weapon(override val id: String, val title: String, override val tags: SpikeTags) :
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

    val newTags = game.locations.find { it.id == game.locationId }?.let { location ->
        location.tags.map { locationTag ->
            "location:${location.id}:${locationTag.key}" to locationTag.value
        }.toMap()
    }.orEmpty() + game.player.tags.map {
        "player:${it.key}" to it.value
    }.toMap() + game.npcs.flatMap { npc ->
        npc.tags.map { tag ->
            "npc:${npc.id}:${tag.key}" to tag.value
        }
    }.toMap() + game.tags

    val newPlot = when (action) {
        "speak" -> {
            val npcToSpeak = game.npcs.first()
            val npcName = npcToSpeak.tags.getTagValue("name")
            val npcOccupation = npcToSpeak.tags.getTagValue("occupation")
            when {
                newTags.containsTagKeys(
                    "location:saloon",
                    "dialog:greetings"
                ) -> listOf("$npcName ($npcOccupation): Would you like a drink?")

                newTags.containsTagKey("location:saloon") -> listOf("$npcName ($npcOccupation): Howdy!")
                else -> emptyList()
            }
        }

        else -> emptyList()
    }

    return game.copy(
        tags = newTags,
        plot = newPlot,
    )
}

private fun SpikeTags.getTagValue(key: String) = entries.find { tagEntry ->
    tagEntry.key.contains(key)
}?.value

private fun SpikeTags.containsTagKeys(vararg keys: String) = keys.all { key ->
    any { tagEntry -> tagEntry.key.contains(key) }
}

private fun SpikeTags.containsTagKey(key: String) = any { tagEntry -> tagEntry.key.contains(key) }

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

fun weapon(id: String = "weapon", title: String = "", tags: SpikeTags = tags()) = Weapon(
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
