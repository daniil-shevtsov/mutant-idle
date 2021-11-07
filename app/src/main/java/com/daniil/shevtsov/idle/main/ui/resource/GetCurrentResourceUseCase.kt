package com.daniil.shevtsov.idle.main.ui.resource

import com.daniil.shevtsov.idle.main.domain.resource.ResourceRepository
import javax.inject.Inject

class GetCurrentResourceUseCase @Inject constructor(
    private val resourceRepository: ResourceRepository,
) {

    suspend operator fun invoke() = resourceRepository.getCurrentResource()

}