package com.daniil.shevtsov.idle.core.domain

import com.daniil.shevtsov.idle.core.BalanceConfig

fun balanceConfig(
    tickRateMillis: Long = 0L,
    resourcePerMillisecond: Double = 0.0,
    resourceSpentForFullMutant: Double = 0.0,
    detectiveSuspicionMultiplier: Double = 0.0,
) = BalanceConfig(
    tickRateMillis = tickRateMillis,
    resourcePerMillisecond = resourcePerMillisecond,
    resourceSpentForFullMutant = resourceSpentForFullMutant,
    detectiveSuspicionMultiplier = detectiveSuspicionMultiplier,
)
