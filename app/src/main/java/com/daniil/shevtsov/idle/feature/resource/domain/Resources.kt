package com.daniil.shevtsov.idle.feature.resource.domain

fun createResources() = ResourceKey.values().map { key ->
    val value = when(key) {
        ResourceKey.Blood -> 1000.0 //TODO: For debug
        else -> 0.0
    }
    Resource(
        key = key,
        name = key.name,
        value = value,
    )
}
