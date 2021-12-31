package com.daniil.shevtsov.idle.feature.resource.domain

data class Resource(
    val key : ResourceKey,
    val name: String,
    val value: Double,
)