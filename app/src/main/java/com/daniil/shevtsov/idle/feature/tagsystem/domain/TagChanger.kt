package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey

interface TagChanger {
    val id: String
    val tagChanges: SpikeTags
}

fun SpikeTags.apply(entry: TagChanger): SpikeTags = toMutableMap().apply {
    remove(tagKey(key = "current action"))

    entry.tagChanges.forEach { (tag, valueToAdd) ->
        val oldValue = get(tag)
        val newValue = when {
            valueToAdd.value.contains('+') -> {
                val oldValueWithoutBrackets = oldValue?.value
                    ?.substringAfter('[')
                    ?.substringBefore(']')
                val valueToAddWithoutBrackets =
                    valueToAdd.value
                        .substringAfter('[')
                        .substringBefore(']')
                when (oldValueWithoutBrackets) {
                    null -> "[$valueToAddWithoutBrackets]"
                    else -> "[$oldValueWithoutBrackets,$valueToAddWithoutBrackets]"
                }
            }

            valueToAdd.value.contains("\${-") && oldValue != null -> {
                val oldValueNumber = oldValue.value.toInt()
                val valueToAddNumber =
                    valueToAdd.value.substringAfter('{').substringBefore('}').toInt()
                (oldValueNumber + valueToAddNumber).toString()
            }

            else -> valueToAdd.value
        }
        put(tag, valueToAdd.copy(value = newValue))
    }
}
