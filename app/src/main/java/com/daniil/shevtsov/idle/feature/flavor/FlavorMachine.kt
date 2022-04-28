package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun flavorMachine(
    original: String,
    tags: List<Tag> = emptyList(),
): String {
    val peopleNameFlavor = when {
        tags.contains(Tags.Immortal) -> "mere mortals"
        tags.contains(Tags.Species.Alien) -> "primitive life forms"
        else -> "people"
    }

    return flavorMinivan(original = original, flavor = Flavors.newInvisibilityAction, tags = tags)
        .replace(Flavors.DerogativePeopleName, peopleNameFlavor)
}

fun flavorMinivan(
    original: String,
    flavor: Flavor,
    tags: List<Tag>,
): String {
//    original = original,
//    flavor = Flavors.newInvisibilityAction,
//    tags = listOf(Tags.Nature.Tech),
    val placeholder = flavor.placeholder
    val replacement =
        tags.firstOrNull { tag -> flavor.values.containsKey(tag) }?.let { flavor.values[it] }
            ?: flavor.default
    return original.replace(placeholder, replacement)
}
