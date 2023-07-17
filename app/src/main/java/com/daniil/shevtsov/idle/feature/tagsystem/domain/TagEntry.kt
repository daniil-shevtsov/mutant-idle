package com.daniil.shevtsov.idle.feature.tagsystem.domain

interface TagEntry : TagRequirer, TagChanger {
    override val id: String
    override val requiredTags: SpikeTags
    override val tagChanges: SpikeTags
    val weight: Float
}

fun <T : TagEntry> List<T>.allMostSuitableFor(presentTags: SpikeTags): List<T> =
    filter { line -> line.suitableFor(presentTags) }
        .sortedWith(compareBySuitability(presentTags))
        .reversed()

fun <T : TagEntry> List<T>.mostSuitableFor(presentTags: SpikeTags): T? =
    allMostSuitableFor(presentTags)
        .firstOrNull()
fun compareBySuitability(presentTags: SpikeTags): Comparator<TagEntry> = compareBy(
    { entry -> entry.weight },
    { entry ->
        entry.requiredTags
            .count { (key, value) -> presentTags[key] == value }
    }
)
