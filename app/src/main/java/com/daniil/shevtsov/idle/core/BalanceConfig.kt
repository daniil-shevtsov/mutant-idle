package com.daniil.shevtsov.idle.core

data class BalanceConfig(
    val tickRateMillis: Long,
    val resourcePerTick: Double,
)