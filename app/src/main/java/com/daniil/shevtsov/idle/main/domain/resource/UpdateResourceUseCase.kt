package com.daniil.shevtsov.idle.main.domain.resource

import com.daniil.shevtsov.idle.main.data.time.Time
import javax.inject.Inject

class UpdateResourceUseCase @Inject constructor(
    private val resourceRepository: ResourceRepository,
) {
    suspend operator fun invoke(
        timePassed: Time
    ) {
        val oldResource = resourceRepository.getCurrentResource()
        val newResource = Resource(value = oldResource.value + 1 * timePassed.value)
        resourceRepository.setNewResource(newResource)
    }
}