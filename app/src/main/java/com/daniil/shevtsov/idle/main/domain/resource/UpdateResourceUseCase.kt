package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.main.data.time.Time
import javax.inject.Inject

class UpdateResourceUseCase @Inject constructor(
    private val calculateResourceIncrease: CalculateResourceIncreaseUseCase,
    private val resourceRepository: ResourceRepository,
) {
    suspend operator fun invoke(
        timePassed: Time
    ) {
        val gain = calculateResourceIncrease(
            passedTicks = timePassed.value,
        )

        resourceRepository.increaseBy(gain)
    }
}