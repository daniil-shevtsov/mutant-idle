package com.daniil.shevtsov.idle.core

data class BalanceConfig(
    val tickRateMillis: Long,
    val resourcePerMillisecond: Double,
    val resourceSpentForFullMutant: Double,
    val detectiveSuspicionMultiplier: Double,
)
