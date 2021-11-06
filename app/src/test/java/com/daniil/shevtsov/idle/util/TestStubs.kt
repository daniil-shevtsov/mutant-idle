package com.daniil.shevtsov.idle.util

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.domain.resource.Resource

fun balanceConfig(
    tickRateMillis: Long = 1L,
    resourcePerMillisecond: Double = 1.0,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
    resourcePerMillisecond = resourcePerMillisecond,
)

fun time(value: Long = 0L) = Time(value = value)

fun resource(value: Double = 0.0) = Resource(value = value)
