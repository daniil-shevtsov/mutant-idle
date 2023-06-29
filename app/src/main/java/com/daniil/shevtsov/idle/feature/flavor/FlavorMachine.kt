package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun flavorMachine(
    original: String,
    flavors: List<Flavor>,
    tags: List<Tag> = emptyList(),
): String {
    var newOriginal = original
    val replacedFlavors = mutableSetOf<Flavor>()
    var isStuck = false

    while (!isStuck && newOriginal.contains(Flavors.PREFIX)) {
        flavors.forEach { flavor ->
            if (flavor in replacedFlavors) {
                println("stuck")
                isStuck = true

            }
            newOriginal = flavorMinivan(original = newOriginal, flavor = flavor, tags = tags)
            replacedFlavors += flavor
        }
    }

    return newOriginal
}

fun flavorMinivan(
    original: String,
    flavor: Flavor,
    tags: List<Tag>,
): String {
    val placeholder = flavor.placeholder
    val replacement =
        tags.firstOrNull { tag -> flavor.values.containsKey(tag) }?.let { flavor.values[it] }
            ?: flavor.default
    return original.replace(placeholder, replacement)
}
