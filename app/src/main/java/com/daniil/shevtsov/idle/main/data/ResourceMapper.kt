package com.daniil.shevtsov.idle.main.data

import com.daniil.shevtsov.idle.main.domain.Resource

internal object ResourceMapper {
    fun map(dto: ResourceDto): Resource = with(dto) {
        Resource(value = value)
    }
}