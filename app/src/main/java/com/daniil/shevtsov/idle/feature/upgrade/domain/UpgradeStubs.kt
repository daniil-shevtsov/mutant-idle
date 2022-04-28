package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation

fun upgrade(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    price: Double = 0.0,
    status: UpgradeStatus = UpgradeStatus.NotBought,
    tags: Map<Tag, TagRelation> = mapOf(),
    newTags: Map<TagRelation, List<Tag>> = mapOf(),
) = Upgrade(
    id = id,
    title = title,
    subtitle = subtitle,
    price = price(value = price),
    status = status,
    tags = if(newTags.isNotEmpty()) {
        newTags
    } else {
        tags.map { it.value to listOf(it.key) }.toMap()
    },
)

fun price(
    value: Double = 0.0,
) = Price(
    value = value,
)
