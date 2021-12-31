package com.daniil.shevtsov.idle.feature.resource.domain

data class Resource(
    val key : ResourceKey = ResourceKey.Blood,
    val name: String = "Blood",
    val value: Double,
)