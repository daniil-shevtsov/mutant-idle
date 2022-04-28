package com.daniil.shevtsov.idle.util

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey

fun balanceConfig(
    tickRateMillis: Long = 1L,
    resourcePerMillisecond: Double = 1.0,
    resourceSpentForFullMutant: Double = 1000.0,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
    resourcePerMillisecond = resourcePerMillisecond,
    resourceSpentForFullMutant = resourceSpentForFullMutant,
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
