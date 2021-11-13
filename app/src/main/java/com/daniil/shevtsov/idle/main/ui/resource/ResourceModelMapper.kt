package com.daniil.shevtsov.idle.main.ui.resource

import com.daniil.shevtsov.idle.main.domain.resource.Resource
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

    private fun Double.formatRound(

    ): String = DecimalFormat("#.###")
        .apply { roundingMode = RoundingMode.CEILING }
        .format(this)

}