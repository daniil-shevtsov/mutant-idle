package com.daniil.shevtsov.idle.feature.resource.presentation

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey

data class ResourceModel(
    val key: ResourceKey,
    val name: String,
    val value: String,
    val icon: String,
)
