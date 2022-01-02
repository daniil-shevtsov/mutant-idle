package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ResourceBehavior {

    fun getCurrentResource(
        resourcesStorage: ResourcesStorage,
    ): Resource {
        return resourcesStorage.getByKey(key = ResourceKey.Blood)!!
    }

    fun updateResource(
        resourcesStorage: ResourcesStorage,
        passedTime: Time,
        rate: Double,
    ) {
        val oldResource = resourcesStorage.getByKey(key = ResourceKey.Blood)!!
        val oldValue = oldResource.value
        val newValue = oldValue + passedTime.value * rate

        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = oldResource.copy(value = newValue)
        )
    }

    fun observeAllResources(resourcesStorage: ResourcesStorage) = resourcesStorage.observeAll()

    fun observeResource(
        resourcesStorage: ResourcesStorage,
        key: ResourceKey,
    ): Flow<Resource> {
        return resourcesStorage.observeAll().map { it.find { it.key == key }!! }
    }

    fun decreaseResource(
        amount: Double,
        resourcesStorage: ResourcesStorage,
    ) {
        val oldResource = resourcesStorage.getByKey(key = ResourceKey.Blood)!!
        val oldValue = oldResource.value
        val newValue = oldValue - amount

        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = oldResource.copy(value = newValue)
        )
    }

    fun applyResourceChange(
        amount: Double,
        resourceKey: ResourceKey = ResourceKey.Blood,
        resourcesStorage: ResourcesStorage,
    ) {
        val oldResource = resourcesStorage.getByKey(key = resourceKey)!!
        val oldValue = oldResource.value
        val newValue = oldValue + amount

        resourcesStorage.updateByKey(
            key = resourceKey,
            newValue = oldResource.copy(value = newValue)
        )
    }

}