package com.daniil.shevtsov.idle.feature.resource.domain

fun createResources() = ResourceKey.values().map { key ->
    Resource(
        key = key,
        name = key.name,
        value = 0.0,
    )
}
