package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.time.Time
import javax.inject.Inject

class UpdateResourceUseCase @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val resourceRepository: ResourceRepository,
) {
    suspend operator fun invoke(
        timePassed: Time
    ) {
        val gain = timePassed.value * balanceConfig.resourcePerMillisecond

        resourceRepository.increaseBy(gain)
    }
}