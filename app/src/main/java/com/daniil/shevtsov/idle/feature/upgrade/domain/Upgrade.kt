package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

data class Upgrade(
    val id: Long,
    val title: String,
    val subtitle: String,
    val price: Price,
    val resourceChanges: Map<ResourceKey, Double>,
    val status: UpgradeStatus,
    val tags: Map<TagRelation, List<Tag>>,
)
