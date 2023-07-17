package com.daniil.shevtsov.idle.feature.tagsystem.domain

private typealias Plot = String

data class PerformResult(
    val tags: SpikeTags,
    val plot: List<Plot>,
)

fun performm(game: Game, action: String? = null): Game {
    return update(
        game,
        action = action ?: game.tags[tagKey("current action")]?.value ?: "default"
    )
}

fun newPerform(game: Game, action: String? = null): Game {
    var kek = performm(game, action)
    var count = 0
    while (!kek.plot.contains("no suitable entry") && count < 20) {
        val lol = performm(kek)
        kek = lol
        count++
    }
    return kek.copy(
        plot = kek.plot.filter { it != "no suitable entry" }
    )
}

fun SpikeTags.containsKey(key: String) = any { (tagKey, _) -> tagKey.tagKey == key }

private fun perform(game: Game): PerformResult {
    val presentTags = game.tags

    val lines = game.lines.map { line ->
        when (line.requiredTags.containsKey("current action")) {
            true -> line.copy(
                entry = line.entry.copy(
                    weight = line.entry.weight + 100f
                )
            )

            false -> line
        }
    }

    val sortedEntries = lines.allMostSuitableFor(presentTags)

    val mostSuitableEntry =
        (sortedEntries.firstOrNull() as? Line)?.entry ?: entry("no suitable entry")
    val mostSuitableLine = mostSuitableEntry.plot

    val modifiedTags = presentTags.apply(mostSuitableEntry)

    return PerformResult(
        tags = modifiedTags,
        plot = game.plot + mostSuitableLine,
    )
}

fun <T : TagEntry> List<T>.allMostSuitableFor(presentTags: SpikeTags): List<T> =
    filter { line -> line.suitableFor(presentTags) }
        .sortedWith(compareBySuitability(presentTags))
        .reversed()

fun <T : TagEntry> List<T>.mostSuitableFor(presentTags: SpikeTags): T? =
    allMostSuitableFor(presentTags)
        .firstOrNull()

fun SpikeTags.apply(entry: TagChanger): SpikeTags = toMutableMap().apply {
    remove(tagKey(key = "current action"))

    entry.tagChanges.forEach { (tag, valueToAdd) ->
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

fun compareBySuitability(presentTags: SpikeTags): Comparator<TagEntry> = compareBy(
    { entry -> entry.weight },
    { entry ->
        entry.requiredTags
            .count { (key, value) -> presentTags[key] == value }
    }
)

fun TagRequirer.suitableFor(
    presentTags: SpikeTags
): Boolean =
    requiredTags == listOf("" to "") || requiredTags.all { (requiredTag, requiredTagValue) ->
        val currentTags = presentTags
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

data class Location(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Container(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Player(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Item(override val id: String, val title: String, override val tags: SpikeTags) :
    TagHolder

data class Npc(override val id: String, val title: String, override val tags: SpikeTags) : TagHolder
data class Line(
    override val id: String,
    override val requiredTags: SpikeTags,
    val entry: LineEntry,
) : TagEntry {
    override val tagChanges: SpikeTags
        get() = entry.tagChanges
    override val weight: Float
        get() = entry.weight
}

fun line(
    id: String = "",
    requiredTags: SpikeTags,
    entry: LineEntry,
) = Line(id, requiredTags, entry)

data class LineEntry(
    override val id: String,
    override val tagChanges: SpikeTags,
    val plot: Plot,
    val weight: Float,
) : TagChanger

fun entry(
    plot: String,
    id: String = "",
    tagChanges: SpikeTags = mapOf(),
    weight: Float = 1.0f
): LineEntry = LineEntry(
    id = id,
    tagChanges = tagChanges,
    plot = plot,
    weight = weight,
)

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
) = DialogLine(
    id = id,
    text = text,
    requiredTags = requiredTags,
    tagChanges = tagChanges
).let { dialogLine ->
    line(
        id = dialogLine.id,
        requiredTags = dialogLine.requiredTags,
        entry = entry(
            plot = dialogLine.text,
            id = dialogLine.id,
            tagChanges = dialogLine.tagChanges,
            weight = 1.0f,
        )
    )
}

data class Game(
    override val id: String,
    val lines: List<Line>,
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

    val performResult = perform(game.copy(tags = newTags))
    val finalPlot = when (action) {
        "speak" -> {
            val npcToSpeak = game.npcs.first()
            val npcName = npcToSpeak.tags.getTagValue(tagKey("name"))?.value
            val npcOccupation = npcToSpeak.tags.getTagValue(tagKey("occupation"))?.value
            val speakerIndication = "$npcName ($npcOccupation):"

            val finalLine = performResult.plot.lastOrNull()?.let { line ->
                "$speakerIndication $line"
            }

            game.plot + finalLine?.let { listOf(finalLine) }.orEmpty()
        }

        else -> performResult.plot
    }

    return game.copy(
        tags = performResult.tags,
        plot = finalPlot,
    )
}

private fun SpikeTags.getTagValue(key: SpikeTagKey) = (entries.find { (tagKey, tag) ->
    tagKey == key
} ?: entries.find { (tagKey, tag) ->
    tagKey.tagKey.contains(key.tagKey)
})?.value

fun game(
    id: String = "game",
    locations: List<Location> = emptyList(),
    locationId: String = "",
    player: Player = player(),
    lines: List<Line> = emptyList(),
    npcs: List<Npc> = emptyList(),
    tags: SpikeTags = tags(),
    plot: List<String> = emptyList(),
) = Game(
    id = id,
    locationId = locationId,
    locations = locations,
    lines = plotLines + lines,
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

interface TagEntry : TagRequirer, TagChanger {
    override val id: String
    override val requiredTags: SpikeTags
    override val tagChanges: SpikeTags
    val weight: Float
}
