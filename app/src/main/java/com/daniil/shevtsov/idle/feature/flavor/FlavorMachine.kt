package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

fun flavorMachine(
    original: String,
    tags: List<Tag> = emptyList(),
): String {
    val flavor = when {
        tags.contains(Tags.Nature.Magic) -> "become ethereal"
        tags.contains(Tags.Nature.Tech) -> "activate the cloaking device"
        else -> "become invisible"
    }

    return original.replace(Flavor.InvisibilityAction, flavor)
}
