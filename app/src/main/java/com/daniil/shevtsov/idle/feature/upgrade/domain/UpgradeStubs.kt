package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

fun upgrade(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    price: Double = 0.0,
    status: UpgradeStatus = UpgradeStatus.NotBought,
    resourceChanges: Map<ResourceKey, Double> = mapOf(),
    tags: Map<TagRelation, List<Tag>> = mapOf(),
) = Upgrade(
    id = id,
    title = title,
    subtitle = subtitle,
    price = price(value = price),
    status = status,
    tags = tags,
)

fun price(
    value: Double = 0.0,
) = Price(
    value = value,
)
