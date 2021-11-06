package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.main.domain.resource.Resource

internal object ResourceMapper {
    fun map(dto: ResourceDto): Resource = with(dto) {
        Resource(value = value)
    }
}