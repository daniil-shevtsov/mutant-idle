package com.daniil.shevtsov.idle.feature.resource.data

import com.daniil.shevtsov.idle.feature.resource.domain.Resource

internal object ResourceMapper {
    fun map(value: Double): Resource = Resource(value = value)
}