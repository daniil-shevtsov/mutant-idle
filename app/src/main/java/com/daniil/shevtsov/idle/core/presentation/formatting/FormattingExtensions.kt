package com.daniil.shevtsov.idle.core.presentation.formatting

import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.formatRound(digits: Int = 3): String = DecimalFormat("#." + (1..digits).map { "#" }.joinToString(separator = ""))
    .apply { roundingMode = RoundingMode.CEILING }
    .format(this)

fun formatEnumName(name: String) = name
    .mapIndexed { index, letter ->
        " ".takeIf { index != 0 && letter.isUpperCase() }.orEmpty() to letter
    }
    .joinToString(separator = "") { (spaceOrEmpty, letter) ->
        spaceOrEmpty + letter
    }
