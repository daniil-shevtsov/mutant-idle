package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Plot
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagValue

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

    val performResult = when {
        action.contains("pick up spear") -> {
            val performResult = perform(game.copy(tags = newTags))
            val itemId = action.substringAfter("pick up ")
            val item = game.items.find { it.id == itemId }!!
            val plot = "you pick up ${item.title}"
            val modifiedTags = spikeTags(
                tagKey("holding") to tagValue(item.id),
                tagKey("weapon type", entityId = item.id) to tagValue("piercing"),
                tagKey("weapon length", entityId = item.id) to tagValue("long"),
                tagKey("throwable", entityId = item.id) to tagValue("true"),
            )
            PerformResult(game.tags + modifiedTags, game.plot + listOf(plot))
        }
        action.contains("pick up sword") -> {
            val performResult = perform(game.copy(tags = newTags))
            val itemId = action.substringAfter("pick up ")
            val item = game.items.find { it.id == itemId }!!
            val plot = "you pick up ${item.title}"
            val modifiedTags = spikeTags(
               tagKey("holding") to tagValue(item.id),
               tagKey("weapon type", entityId = item.id) to tagValue("sharp"),
               tagKey("weapon length", entityId = item.id) to tagValue("long"),
            )
            PerformResult(game.tags + modifiedTags, game.plot + listOf(plot))
        }

        action == "speak" -> {
            val performResult = perform(game.copy(tags = newTags))
            performResult.copy(plot = when (action) {
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
            )
        }

        else -> perform(game.copy(tags = newTags))
    }

    return game.copy(
        tags = performResult.tags,
        plot = performResult.plot,
    )
}

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
    var lastState = performm(game, action)
    var count = 0
    while (!lastState.plot.contains("no suitable entry") && count < 20) {
        val newState = performm(lastState)
        if (newState.plot.lastOrNull() == lastState.plot.lastOrNull()) {
            break
        }
        lastState = newState
        count++
    }
    return lastState.copy(
        plot = lastState.plot.filter { it != "no suitable entry" }
    )
}

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

fun SpikeTags.containsKey(key: String) = any { (tagKey, _) -> tagKey.tagKey == key }
fun SpikeTags.containsEntity(entityId: String) = any { (tagKey, _) -> tagKey.entityId == entityId }

private fun SpikeTags.getTagValue(key: SpikeTagKey) = (entries.find { (tagKey, tag) ->
    tagKey == key
} ?: entries.find { (tagKey, tag) ->
    tagKey.tagKey.contains(key.tagKey)
})?.value
