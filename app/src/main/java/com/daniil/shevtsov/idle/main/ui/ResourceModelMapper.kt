package com.daniil.shevtsov.idle.main.ui

import com.daniil.shevtsov.idle.main.domain.resource.Resource

internal object ResourceModelMapper {

    fun map(resource: Resource) = with(resource) {
        ResourceModel(
            text = value.toString(),
        )
    }

}