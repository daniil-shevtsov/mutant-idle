package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun flavor(
    placeholder: String = "",
    values: Map<Tag, String> = mapOf(),
    default: String = "",
) = Flavor(
    placeholder = placeholder,
    values = values,
    default = default,
)
