package com.daniil.shevtsov.idle.feature.tagsystem.domain

fun createDefaultTags(): Map<SpikeTagKey, SpikeTag> = tags(
    "mobile" to "true",
    "health" to "100",
    "life" to "alive",
)
typealias SpikeTags = Map<SpikeTagKey, SpikeTag>

fun spikeTags(
    vararg entries: Pair<SpikeTagKey, SpikeTagValue>
): SpikeTags = entries.associate { (key, value) ->
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
