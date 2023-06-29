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
    val appliedFlavors = mutableListOf<String>()

    while (!isStuck && newOriginal.containsFlavorPlaceholder()) {
        flavors.forEach { flavor ->
            if (false) {
                appliedFlavors += flavor.id.name
            }

            if (flavor in replacedFlavors) {
                println("stuck")
                isStuck = true
            }
            newOriginal = flavorMinivan(original = newOriginal, flavor = flavor, tags = tags)
            replacedFlavors += flavor
        }
    }

    return if (/*!isStuck*/true) {
        newOriginal
    } else {
        "ERROR: infinite loop of flavors: (${appliedFlavors.joinToString(separator = ", ")})"
    }
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
