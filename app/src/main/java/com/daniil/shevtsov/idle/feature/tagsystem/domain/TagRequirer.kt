package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags

interface TagRequirer {
    val id: String
    val requiredTags: SpikeTags
}

fun String.extractListValues() = substringAfter('[')
    .substringBefore(']')
    .split(',')

fun TagRequirer.suitableFor(
    presentTags: SpikeTags
): Boolean =
    requiredTags == listOf("" to "") || requiredTags.all { (requiredTag, requiredTagValue) ->
        val currentTags = presentTags
        val currentValue = currentTags[requiredTag]

        val currentValues = when {
            currentValue == null -> emptyList()
            currentValue.value.contains('[') -> currentValue.value.extractListValues()

            else -> listOf(currentValue.value)
        }
        when {
            requiredTagValue.value.contains('!') -> !currentValues.contains(
                requiredTagValue.value.drop(1)
            )

            else -> currentValues.contains(requiredTagValue.value)
        }
    }

fun TagEntry.suitableFor(
    presentTags: SpikeTags
): Boolean {
    val subtractionChanges = tagChanges.filter { it.value.value.contains("\${-") }
    val hasRequiredTagValues = subtractionChanges.all { subtractionChange ->
        val presentTag = presentTags[subtractionChange.key]

        presentTag != null && (presentTag.value.extractNumberValue() + subtractionChange.value.value.extractNumberValue()) >= 0
    }
    return hasRequiredTagValues && (this as TagRequirer).suitableFor(presentTags)
}
