package com.daniil.shevtsov.idle.feature.tagsystem.domain

//private typealias Tags = Map<String, String>
//private typealias Tag = Pair<String, String>
private typealias Plot = String


private fun createDefaultTags() = mapOf(
    "mobile" to "true",
    "health" to "100",
    "life" to "alive",
)

fun defaultTagsWithAdditional(vararg tags: Pair<String, String>): Map<String, String> =
    createDefaultTags().withAdditional(*tags)

fun Map<String, String>.withAdditional(vararg tags: Pair<String, String>): Map<String, String> =
    this + tags.toMap()

fun tags(vararg entries: Pair<String, String>): Map<String, String> =
    entries.toList().toMap()

fun entry(
    plot: String,
    tagChange: Map<String, String> = mapOf(),
    weight: Float = 1.0f
): LineEntry =
    LineEntry(
        tags = tagChange,
        plot = plot,
        weight = weight,
    )

data class LineEntry(
    val tags: Map<String, String>,
    val plot: Plot,
    val weight: Float,
)

data class PerformResult(
    val tags: Map<String, String>,
    val plot: Plot,
)

data class Line(
    val requiredTags: Map<String, String>,
    val entry: LineEntry,
)

private fun line(
    requiredTags: Map<String, String>,
    entry: LineEntry,
) = Line(requiredTags, entry)

fun perform(tags: Map<String, String>): PerformResult {
    val lines = listOf(
        line(
            requiredTags = tags("mobile" to "true", "bones" to "broken"),
            entry = entry(
                "Now you can't move",
                tagChange = tags("mobile" to "false"),
                weight = 1000f,
            ),
        ),
        line(
            requiredTags = tags(
                "immortality" to "!true",
                "life" to "alive",
                "health" to "0"
            ),
            entry = entry(
                "You have died",
                tagChange = tags("life" to "dead"),
                weight = 1000f,
            ),
        ),
        line(
            requiredTags = tags("current action" to "stand", "mobile" to "false"),
            entry = entry(
                "You can't get up",
            )
        ),
        line(
            requiredTags = tags(
                "current action" to "stand",
                "posture" to "lying",
                "mobile" to "true",
            ),
            entry = entry(
                "You get up",
                tags("posture" to "standing")
            )
        ),
        line(
            requiredTags = tags(
                "current action" to "regenerate",
                "ability" to "regeneration",
            ),
            entry = entry(
                "You regenerate to full health",
                tags(
                    "bones" to "okay",
                    "mobile" to "true"//TODO: this should happen separately because the bones are healed
                )
            )
        ),
        line(
            requiredTags = tags("current action" to "stand", "mobile" to "true"),
            entry = entry(
                "You are now standing",
                tags("posture" to "standing")
            )
        ),
        line(
            requiredTags = tags("current action" to "stop flying"),
            entry = entry(
                "You stop flying",
                tags("posture" to "standing")
            )
        ),
        line(
            requiredTags = tags("posture" to "standing"),
            entry = entry("You stand, doing nothing")
        ),
        line(
            requiredTags = tags(
                "posture" to "lying",
                "position" to "ground"
            ),
            entry = entry("You lie on the ground, doing nothing")
        ),
        line(
            requiredTags = tags("posture" to "lying"),
            entry = entry("You lie, doing nothing")
        ),
        line(
            requiredTags = tags("position" to "low-air", "posture" to "flying"),
            entry = entry("You fly in low-air")
        ),
        line(
            requiredTags = tags("position" to "low-air", "indestructible" to "true"),
            entry = entry(
                "You fall to the ground",
                tagChange = tags("position" to "ground", "posture" to "lying")
            ),
        ),
        line(
            requiredTags = tags("position" to "low-air"),
            entry = entry(
                "You fall to the ground, breaking every bone in your body",
                tagChange = tags(
                    "position" to "ground",
                    "posture" to "lying",
                    "bones" to "broken",
                    "health" to "0",
                )
            ),
        ),
        line(
            requiredTags = tags("current action" to "fly"),
            entry = entry(
                "You start flying",
                tags("posture" to "flying", "position" to "low-air")
            )
        ),
        line(requiredTags = tags("" to ""), entry = entry("You do nothing")),
    ).map { line ->
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

                when {
                    requiredTagValue.contains('!') -> currentValue != requiredTagValue.drop(1)
                    else -> currentValue == requiredTagValue
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

    val mostSuitableEntry = sortedEntries.first().entry
    val mostSuitableLine = mostSuitableEntry.plot

    return PerformResult(
        tags = tags.toMutableMap().apply { remove("current action") } + mostSuitableEntry.tags,
        plot = mostSuitableLine,
    )
}
