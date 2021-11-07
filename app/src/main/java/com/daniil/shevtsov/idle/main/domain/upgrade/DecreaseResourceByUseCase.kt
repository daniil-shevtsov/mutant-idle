package com.daniil.shevtsov.idle.main.domain.upgrade

import com.daniil.shevtsov.idle.main.domain.resource.ResourceRepository
import javax.inject.Inject

class DecreaseResourceByUseCase @Inject constructor(
    private val resourceRepository: ResourceRepository
) {
    suspend operator fun invoke(value: Double) {
        resourceRepository.decreaseBy(value)
    }
}
