package com.daniil.shevtsov.idle.feature.resource.domain

import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ResourceBehavior {

    suspend fun getCurrentResource(
        storage: ResourceStorage,
        resourcesStorage: ResourcesStorage,
    ): Resource {
        return resourcesStorage.getByKey(key = ResourceKey.Blood)!!
//        return storage.getCurrentValue().let { Resource(value = it) }
    }

    suspend fun updateResource(
        storage: ResourceStorage,
        resourcesStorage: ResourcesStorage,
        passedTime: Time,
        rate: Double,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue + passedTime.value * rate

        storage.setNewValue(newValue)

        val oldResource = resourcesStorage.getByKey(key = ResourceKey.Blood)
        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = oldResource!!.copy(value = newValue)
        )
    }

    fun observeResource(
        storage: ResourceStorage,
        resourcesStorage: ResourcesStorage,
    ): Flow<Resource> {
        return storage.observeChange().map { Resource(value = it) }
//        return resourcesStorage.observeAll().map { it.find { it.key == ResourceKey.Blood }!! }
    }

    suspend fun decreaseResource(
        storage: ResourceStorage,
        amount: Double,
        resourcesStorage: ResourcesStorage,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue - amount

        storage.setNewValue(newValue)

        val oldResource = resourcesStorage.getByKey(key = ResourceKey.Blood)
        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = oldResource!!.copy(value = newValue)
        )
    }

    suspend fun applyResourceChange(
        storage: ResourceStorage,
        amount: Double,
        resourcesStorage: ResourcesStorage,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue + amount

        storage.setNewValue(newValue)

        val oldResource = resourcesStorage.getByKey(key = ResourceKey.Blood)
        resourcesStorage.updateByKey(
            key = ResourceKey.Blood,
            newValue = oldResource!!.copy(value = newValue)
        )
    }

}