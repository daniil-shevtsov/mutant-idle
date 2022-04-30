package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun flavor(
    id: FlavorId = FlavorId.InvisibilityAction,
    placeholder: String = "",
    values: Map<Tag, String> = mapOf(),
    default: String = "",
) = Flavor(
    id = id,
    placeholder = placeholder,
    values = values,
    default = default,
)
