package com.daniil.shevtsov.idle.main.domain

import javax.inject.Inject

class ObserveResourceUseCase @Inject constructor(
    private val resourceRepository: ResourceRepository,
) {

    operator fun invoke() = resourceRepository.observeResource()

}