package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName

fun createResources() = ResourceKey.values().map { key ->
    Resource(
        key = key,
        name = formatEnumName(key.name),
        value = when (key) {
            ResourceKey.Blood -> 100.0
            ResourceKey.Information -> 1.0
            else -> 0.0
        },
    )
}
