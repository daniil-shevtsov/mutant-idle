package com.daniil.shevtsov.idle.util

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.feature.upgrade.domain.Price
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus

fun balanceConfig(
    tickRateMillis: Long = 1L,
    resourcePerMillisecond: Double = 1.0,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
    resourcePerMillisecond = resourcePerMillisecond,
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

fun price(value: Double = 0.0) = Price(
    value = value,
)

fun resource(value: Double = 0.0) = Resource(value = value)
