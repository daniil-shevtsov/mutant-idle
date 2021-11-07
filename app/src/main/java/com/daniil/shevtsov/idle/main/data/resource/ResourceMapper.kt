package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.main.domain.resource.Resource

internal object ResourceMapper {
    fun map(value: Double): Resource = Resource(value = value)
}