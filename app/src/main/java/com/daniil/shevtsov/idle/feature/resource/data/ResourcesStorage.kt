package com.daniil.shevtsov.idle.feature.resource.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@AppScope
class ResourcesStorage @Inject constructor(
    initialResources: List<Resource>
) {
    private val storedData: MutableStateFlow<Map<ResourceKey, Resource>> =
        MutableStateFlow(initialResources.associateBy { it.key })

    fun observeAll(): Flow<List<Resource>> {
        return storedData.map { it.values.toList() }
    }

    fun getByKey(key: ResourceKey): Resource? {
        val map = storedData.value
        return map[key]
    }

    fun updateByKey(key: ResourceKey, newValue: Resource) {
        val map = storedData.value
        val modifiedMap = map
            .toMutableMap()
            .apply { put(key, newValue) }
            .toMap()
        storedData.value = modifiedMap
    }

}