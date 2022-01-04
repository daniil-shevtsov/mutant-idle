package com.daniil.shevtsov.idle.core.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MultipleStorage<Key, Value> @Inject constructor(
    initialResources: List<Value>,
    keyExtractor: (value: Value) -> Key,
) {
    private val storedData: MutableStateFlow<Map<Key, Value>> =
        MutableStateFlow(initialResources.associateBy { keyExtractor(it) })

    fun observeAll(): Flow<List<Value>> {
        return storedData.map { it.values.toList() }
    }

    fun getByKey(key: Key): Value? {
        val map = storedData.value
        return map[key]
    }

    fun updateByKey(key: Key, newValue: Value) {
        val map = storedData.value
        val modifiedMap = map
            .toMutableMap()
            .apply { put(key, newValue) }
            .toMap()
        storedData.value = modifiedMap
    }

}