package com.daniil.shevtsov.idle.feature.resource.data

import com.daniil.shevtsov.idle.feature.resource.domain.Resource

internal object ResourceDtoMapper {
    fun map(resource: Resource): ResourceDto = with(resource) {
        ResourceDto(value = value)
    }
}