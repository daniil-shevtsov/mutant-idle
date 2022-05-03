package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel

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

fun resourceModel(
    key: ResourceKey = ResourceKey.Blood,
    name: String = "",
    value: String = "",
    icon: String = "",
) = ResourceModel(
    key = key,
    name = name,
    value = value,
    icon = icon,
)
