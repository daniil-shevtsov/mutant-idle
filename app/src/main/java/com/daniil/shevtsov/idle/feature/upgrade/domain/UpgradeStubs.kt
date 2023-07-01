package com.daniil.shevtsov.idle.feature.upgrade.domain

import com.daniil.shevtsov.idle.feature.action.domain.RatioChanges
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceChanges
import com.daniil.shevtsov.idle.feature.resource.domain.noResourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelations
import com.daniil.shevtsov.idle.feature.tagsystem.domain.noTagRelations

fun upgrade(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    price: Double = 0.0,
    status: UpgradeStatus = UpgradeStatus.NotBought,
    resourceChanges: ResourceChanges = noResourceChanges(),
    ratioChanges: RatioChanges = mapOf(),
    tagRelations: TagRelations = noTagRelations(),
    plot: String? = null,
) = Upgrade(
    id = id,
    title = title,
    subtitle = subtitle,
    price = price(value = price),
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    status = status,
    tagRelations = tagRelations,
    plot = plot,
)

fun price(
    value: Double = 0.0,
) = Price(
    value = value,
)
