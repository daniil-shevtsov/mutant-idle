package com.daniil.shevtsov.idle.feature.action.domain

import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Action(
    val id: Long,
    val title: String,
    val subtitle: String,
    val resourceChanges: Map<ResourceKey, Double>,
    val ratioChanges: Map<RatioKey, Double>,
    val tags: Map<TagRelation, List<Tag>>,
)
