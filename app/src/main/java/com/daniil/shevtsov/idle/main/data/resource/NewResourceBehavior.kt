package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.domain.resource.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object NewResourceBehavior {

    suspend fun getCurrentResource(storage: ResourceStorage): Resource {
        return storage.getCurrentValue().let(::Resource)
    }

    suspend fun updateResource(
        storage: ResourceStorage,
        passedTime: Time,
        rate: Double,
    ) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue + passedTime.value * rate
        return storage.setNewValue(newValue)
    }

    fun observeResource(storage: ResourceStorage): Flow<Resource> {
        return storage.observeChange().map(::Resource)
    }

    suspend fun decreaseResource(storage: ResourceStorage, amount: Double) {
        val oldValue = storage.getCurrentValue()
        val newValue = oldValue - amount
        return storage.setNewValue(newValue)
    }

}