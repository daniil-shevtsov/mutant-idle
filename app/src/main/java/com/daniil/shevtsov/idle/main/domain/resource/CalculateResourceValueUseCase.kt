package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.core.BalanceConfig
import javax.inject.Inject

class CalculateResourceValueUseCase @Inject constructor(
    private val balanceConfig: BalanceConfig,
) {

    operator fun invoke(
        oldValue: Long,
        passedTicks: Long,
    ): Long {
        return oldValue + passedTicks * balanceConfig.resourcePerTick.toLong()
    }

}