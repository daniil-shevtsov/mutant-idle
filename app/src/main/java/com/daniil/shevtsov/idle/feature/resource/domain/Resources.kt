package com.daniil.shevtsov.idle.feature.resource.domain

fun createResources() = ResourceKey.values().map { key ->
    Resource(
        key = key,
        name = key.name
            .mapIndexed { index, letter ->
                " ".takeIf { index != 0 && letter.isUpperCase() }.orEmpty() to letter
            }
            .joinToString(separator = "") { (spaceOrEmpty, letter) ->
                spaceOrEmpty + letter
            },
        value = 0.0,
    )
}
