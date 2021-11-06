package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.core.BalanceConfig
import javax.inject.Inject

class CalculateResourceValueUseCase @Inject constructor(
    private val balanceConfig: BalanceConfig,
) {

    operator fun invoke(
        oldValue: Double,
        passedTicks: Long,
    ): Double {
        return oldValue + passedTicks * balanceConfig.resourcePerMillisecond
    }

}