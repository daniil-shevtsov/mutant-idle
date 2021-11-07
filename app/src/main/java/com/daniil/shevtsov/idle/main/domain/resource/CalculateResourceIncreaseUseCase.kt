package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.core.BalanceConfig
import javax.inject.Inject

class CalculateResourceIncreaseUseCase @Inject constructor(
    private val balanceConfig: BalanceConfig,
) {

    operator fun invoke(
        passedTicks: Long,
    ): Double {
        return passedTicks * balanceConfig.resourcePerMillisecond
    }

}