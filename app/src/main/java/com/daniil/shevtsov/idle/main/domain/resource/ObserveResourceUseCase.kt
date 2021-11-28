package com.daniil.shevtsov.idle.main.domain.resource

import javax.inject.Inject

class ObserveResourceUseCase @Inject constructor(
    private val resourceRepository: ResourceRepository,
) : ResourceSource {

    override operator fun invoke() = resourceRepository.observeResource()

}