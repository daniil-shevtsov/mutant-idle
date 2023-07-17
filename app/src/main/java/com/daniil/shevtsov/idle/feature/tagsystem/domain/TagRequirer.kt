package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags

interface TagRequirer {
    val id: String
    val requiredTags: SpikeTags
}

fun TagRequirer.suitableFor(
    presentTags: SpikeTags
): Boolean = requiredTags == listOf("" to "") || requiredTags.all { (requiredTag, requiredTagValue) ->
        val currentTags = presentTags
        val currentValue = currentTags[requiredTag]

        val currentValues = when {
            currentValue == null -> emptyList()
            currentValue.value.contains('[') -> currentValue.value
                .substringAfter('[')
                .substringBefore(']')
                .split(',')

            else -> listOf(currentValue.value)
        }
        when {
            requiredTagValue.value.contains('!') -> !currentValues.contains(
                requiredTagValue.value.drop(1)
            )

            else -> currentValues.contains(requiredTagValue.value)
        }
    }
