package com.daniil.shevtsov.idle.feature.resource.presentation

import com.daniil.shevtsov.idle.core.presentation.formatting.formatRound
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey

internal object ResourceModelMapper {

    fun map(
        resource: Resource,
        name: String,
    ) = with(resource) {
        ResourceModel(
            key = key,
            name = name,
            value = value.formatRound(),
            icon = chooseIconFor(key),
        )
    }

    private fun chooseIconFor(key: ResourceKey) = when (key) {
        ResourceKey.Blood -> Icons.Blood
        ResourceKey.Money -> Icons.Money
        ResourceKey.HumanFood -> Icons.HumanFood
        ResourceKey.Prisoner -> Icons.Prisoner
        ResourceKey.Remains -> Icons.Remains
        ResourceKey.FreshMeat -> Icons.FreshMeat
        ResourceKey.Organs -> Icons.Organs
    }

}
