package com.daniil.shevtsov.idle.feature.flavor

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Flavor(
    val placeholder: String,
    val values: Map<Tag, String>,
    val default: String,
)
