package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Action(
    val id: Long,
    val title: String,
    val subtitle: String,
    val actionType: ActionType,
    val resourceChanges: Map<ResourceKey, Double>,
    val ratioChanges: Map<RatioKey, Float> = mapOf(),
    val tags: List<Tag> = emptyList(),
)