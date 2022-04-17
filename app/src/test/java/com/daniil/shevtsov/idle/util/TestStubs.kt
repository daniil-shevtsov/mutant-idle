package com.daniil.shevtsov.idle.util

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.action.domain.Action
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.feature.upgrade.domain.Price
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun balanceConfig(
    tickRateMillis: Long = 1L,
    resourcePerMillisecond: Double = 1.0,
    resourceSpentForFullMutant: Double = 1000.0,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
    resourcePerMillisecond = resourcePerMillisecond,
    resourceSpentForFullMutant = resourceSpentForFullMutant,
)

fun time(value: Long = 0L) = Time(value = value)

fun upgrade(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    price: Double = 0.0,
    status: UpgradeStatus = UpgradeStatus.NotBought,
) = Upgrade(
    id = id,
    title = title,
    subtitle = subtitle,
    price = price(value = price),
    status = status,
)

fun action(
    id: Long = 0L,
    title: String = "",
    subtitle: String = "",
    actionType: ActionType = ActionType.Human,
    resourceChanges: Map<ResourceKey, Double> = mapOf(),
    ratioChanges: Map<RatioKey, Float> = mapOf(),
    tags: Map<Tag, TagRelation> = mapOf(),
) = Action(
    id = id,
    title = title,
    subtitle = subtitle,
    actionType = actionType,
    resourceChanges = resourceChanges,
    ratioChanges = ratioChanges,
    tags = tags,
)

fun price(value: Double = 0.0) = Price(
    value = value,
)

fun resource(
    key: ResourceKey = ResourceKey.Blood,
    name: String = "",
    value: Double = 0.0,
) = Resource(
    key = key,
    name = name,
    value = value,
)
