package com.daniil.shevtsov.idle.feature.resource.domain

fun resource(
    key: ResourceKey = ResourceKey.Blood,
    name: String = "",
    value: Double = 0.0,
) = Resource(
    key = key,
    name = name,
    value = value,
)

fun resourceChange(
    key: ResourceKey = ResourceKey.Blood,
    change: Double = 0.0
) = key to change
