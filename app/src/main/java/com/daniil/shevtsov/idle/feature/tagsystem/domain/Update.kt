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

    var finalModifiedNpc: Npc? = null

    val performResult = when {
        action.contains("pick up spear") || action.contains("pick up sword") -> {
            val itemId = action.substringAfter("pick up ")
            val item = game.items.find { it.id == itemId }!!
            val plot = "You pick up ${item.title}"
            val tagChanger = object : TagChanger {
                override val id: String
                    get() = "lol"
                override val tagChanges: SpikeTags
                    get() = spikeTags(
                        tagKey("holding") to tagValue(item.id),
                        *item.tags.map {
                            val newKey = it.key.copy(entityId = item.id)
                            newKey to it.value.value
                        }.toTypedArray()
                    )
            }
            PerformResult(game.tags.apply(tagChanger), game.plot + listOf(plot))
        }

        action.contains("target") -> {
            val entityId = action.substringAfter("target ")
            val npc = game.npcs.find { it.id == entityId }
            val heldItem = game.items.find { it.id == game.tags[tagKey("holding")]?.value }
            val plot = "You aim at ${npc?.title} with ${heldItem?.title}"
            val tagChanger = object : TagChanger {
                override val id: String
                    get() = "lol"
                override val tagChanges: SpikeTags
                    get() = spikeTags(
                        tagKey("targeting") to tagValue(entityId),
                        tagKey("holding") to tagValue("null"),
                    )
            }
            PerformResult(game.tags.apply(tagChanger), game.plot + listOf(plot))
        }

        action == "throw" -> {
            val itemId = game.tags[tagKey("holding")]?.value
            val targetId = game.tags[tagKey("targeting")]?.value
            val targetNpc = game.npcs.find { it.id == targetId }
            val item = game.items.find { it.id == itemId }!!
            val plot = "You throw ${item.title} ${
                when (targetNpc) {
                    null -> "in general direction"
                    else -> "at ${targetNpc.title}"
                }
            }"

            val tagChanger = object : TagChanger {
                override val id: String
                    get() = "lol"
                override val tagChanges: SpikeTags
                    get() = spikeTags(
                        tagKey("holding") to tagValue("null")
                    )
            }

            val npcTagChanger = object : TagChanger {
                override val id: String
                    get() = item.id
                override val tagChanges: SpikeTags
                    get() = spikeTags(
                        tagKey("health") to tagValue("\${-90}")
                    )
            }

            //TODO: Need mechanism to remove all tags of particular entity by tag changer
            val tagsToRemove: SpikeTags = game.tags.filter { it.key.entityId == itemId }
            val modifiedTags = game.tags
                .apply(tagChanger)
                .filter { it.key !in tagsToRemove.keys }
            val modifiedTarget = targetNpc?.let {
                it.copy(
                    tags = it.tags.apply(npcTagChanger)
                )
            }
            finalModifiedNpc = modifiedTarget
            PerformResult(modifiedTags, game.plot + listOf(plot))
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
        npcs = when (finalModifiedNpc) {
            null -> game.npcs
            else -> game.npcs.map { npc ->
                when (npc.id) {
                    finalModifiedNpc.id -> finalModifiedNpc
                    else -> npc
                }
            }
        }
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