package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ResourceBehavior {

    private val hackStorage = ResourcesStorage(emptyList())

    suspend fun getCurrentResource(storage: ResourceStorage): Resource {
        return storage.getCurrentValue().let { Resource(value = it) }
    }

    suspend fun updateResource(
        storage: ResourceStorage,
        resourcesStorage: ResourcesStorage = hackStorage,
        passedTime: Time,
        rate: Double,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue + passedTime.value * rate
        return storage.setNewValue(newValue)
    }

    fun observeResource(
        storage: ResourceStorage,
        resourcesStorage: ResourcesStorage,
    ): Flow<Resource> {
        return storage.observeChange().map { Resource(value = it) }
    }

    suspend fun decreaseResource(
        storage: ResourceStorage,
        amount: Double,
        resourcesStorage: ResourcesStorage = hackStorage,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue - amount
        return storage.setNewValue(newValue)
    }

    suspend fun applyResourceChange(
        storage: ResourceStorage,
        amount: Double,
        resourcesStorage: ResourcesStorage = hackStorage,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue + amount
        return storage.setNewValue(newValue)
    }

}