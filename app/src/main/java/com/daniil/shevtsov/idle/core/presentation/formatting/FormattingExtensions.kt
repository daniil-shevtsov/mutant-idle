package com.daniil.shevtsov.idle.core.presentation.formatting

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey

fun formatEnumName(name: String) = name
    .mapIndexed { index, letter ->
        " ".takeIf { index != 0 && letter.isUpperCase() }.orEmpty() to letter
    }
    .joinToString(separator = "") { (spaceOrEmpty, letter) ->
        spaceOrEmpty + letter
    }
