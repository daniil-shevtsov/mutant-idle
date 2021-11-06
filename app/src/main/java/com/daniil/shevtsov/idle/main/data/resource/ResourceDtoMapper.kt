package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.main.domain.resource.Resource

internal object ResourceDtoMapper {
    fun map(resource: Resource): ResourceDto = with(resource) {
        ResourceDto(value = value)
    }
}