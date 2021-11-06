package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.main.data.time.Time
import javax.inject.Inject

class UpdateResourceUseCase @Inject constructor(
    private val calculateResourceValue: CalculateResourceValueUseCase,
    private val resourceRepository: ResourceRepository,
) {
    suspend operator fun invoke(
        timePassed: Time
    ) {
        val newValue = calculateResourceValue(
            oldValue = resourceRepository.getCurrentResource().value,
            passedTicks = timePassed.value,
        )

        resourceRepository.setNewResource(Resource(value = newValue))
    }
}