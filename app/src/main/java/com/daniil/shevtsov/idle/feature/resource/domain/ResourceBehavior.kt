package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ResourceBehavior {

    fun getCurrentResource(
        resourcesStorage: ResourcesStorage,
        resourceKey: ResourceKey = ResourceKey.Blood,
    ): Resource {
        return resourcesStorage.getByKey(key = resourceKey)!!
    }

    fun observeAllResources(resourcesStorage: ResourcesStorage) = resourcesStorage.observeAll()

    fun observeResource(
        resourcesStorage: ResourcesStorage,
        key: ResourceKey,
    ): Flow<Resource> {
        return resourcesStorage.observeAll().map { it.find { it.key == key }!! }
    }

    fun updateResourceByTime(
        resourcesStorage: ResourcesStorage,
        resourceKey: ResourceKey = ResourceKey.Blood,
        passedTime: Time,
        rate: Double,
    ) {
        applyChange(
            resourcesStorage = resourcesStorage,
            resourceKey = resourceKey,
            amount = passedTime.value * rate,
        )
    }

    fun decreaseResource(
        amount: Double,
        resourceKey: ResourceKey = ResourceKey.Blood,
        resourcesStorage: ResourcesStorage,
    ) {
        applyChange(
            resourcesStorage = resourcesStorage,
            resourceKey = resourceKey,
            amount = -amount,
        )
    }

    fun applyResourceChange(
        amount: Double,
        resourceKey: ResourceKey = ResourceKey.Blood,
        resourcesStorage: ResourcesStorage,
    ) {
        applyChange(
            resourcesStorage = resourcesStorage,
            resourceKey = resourceKey,
            amount = amount,
        )
    }

    private fun applyChange(
        resourcesStorage: ResourcesStorage,
        resourceKey: ResourceKey,
        amount: Double
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