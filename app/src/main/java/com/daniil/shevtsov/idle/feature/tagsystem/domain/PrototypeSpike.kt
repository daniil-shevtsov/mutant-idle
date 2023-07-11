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
                    requiredTagValue.contains('!') -> !currentValues.contains(requiredTagValue.drop(1))
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
                    val valueToAddWithoutBrackets = valueToAdd.substringAfter('[').substringBefore(']')
                    "[$oldValueWithoutBrackets,$valueToAddWithoutBrackets]"
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
