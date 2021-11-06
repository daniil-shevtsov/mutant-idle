package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.domain.resource.Resource

internal object ResourceModelMapper {

    fun map(
        resource: Resource,
        name: String,
    ) = with(resource) {
        ResourceModel(
            name = name,
            value = value.toString(),
        )
    }

}