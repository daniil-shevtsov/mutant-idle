package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Line
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Plot
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTagKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.entry
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.spikeTag
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

private fun SpikeTags.getTagValue(key: SpikeTagKey) = (entries.find { (tagKey, tag) ->
    tagKey == key
} ?: entries.find { (tagKey, tag) ->
    tagKey.tagKey.contains(key.tagKey)
})?.value
