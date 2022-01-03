package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey

data class Action(
    val id: Long,
    val title: String,
    val subtitle: String,
    val actionType: ActionType,
    val resourceChanges: Map<ResourceKey, Double> = mapOf(),
)