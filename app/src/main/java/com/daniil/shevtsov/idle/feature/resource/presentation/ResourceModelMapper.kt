package com.daniil.shevtsov.idle.feature.resource.presentation

import com.daniil.shevtsov.idle.core.presentation.formatting.formatRound
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import java.math.RoundingMode
import java.text.DecimalFormat

internal object ResourceModelMapper {

    fun map(
        resource: Resource,
        name: String,
    ) = with(resource) {
        ResourceModel(
            name = name,
            value = value.formatRound(),
        )
    }

}
