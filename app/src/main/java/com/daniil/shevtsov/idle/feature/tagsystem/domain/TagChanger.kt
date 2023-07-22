package com.daniil.shevtsov.idle.feature.tagsystem.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.SpikeTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.tagKey

interface TagChanger {
    val id: String
    val tagChanges: SpikeTags
}

private fun String.extractListValue() = substringAfter("[").substringBefore(']')
private fun String.extractListInsert() = substringAfter("+[").substringBefore(']')

fun SpikeTags.apply(entry: TagChanger): SpikeTags = toMutableMap().apply {
    remove(tagKey(key = "current action"))

    entry.tagChanges.forEach { (tag, valueToAdd) ->
        val oldValue = get(tag)
        val newValue = when {
            valueToAdd.value.contains("+[") -> {
                val currentListValues = oldValue?.value?.extractListValues()
                val valueToAddWithoutBrackets = valueToAdd.value.extractListInsert()
                when (currentListValues) {
                    null -> "[$valueToAddWithoutBrackets]"
                    else -> (currentListValues + valueToAddWithoutBrackets).joinToString(separator = ",", prefix = "[", postfix = "]")
                }
            }

            valueToAdd.value.contains("\${-") && oldValue != null -> {
                val oldValueNumber = oldValue.value.toInt()
                val valueToAddNumber =
                    valueToAdd.value.substringAfter('{').substringBefore('}').toInt()
                (oldValueNumber + valueToAddNumber).toString()
            }

            valueToAdd.value.contains("\${+") -> {
                val oldValueNumber = oldValue?.value?.toInt() ?: 0
                val valueToAddNumber =
                    valueToAdd.value.substringAfter('{').substringBefore('}').toInt()
                (oldValueNumber + valueToAddNumber).toString()
            }

            else -> valueToAdd.value
        }
        put(tag, valueToAdd.copy(value = newValue))
    }
}
