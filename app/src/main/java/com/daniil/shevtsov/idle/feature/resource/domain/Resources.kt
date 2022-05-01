package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.core.presentation.formatting.formatEnumName

fun createResources() = ResourceKey.values().map { key ->
    Resource(
        key = key,
        name = formatEnumName(key.name),
        value = 0.0,
    )
}
