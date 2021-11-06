package com.daniil.shevtsov.idle.main.data.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ResourceStorage @Inject constructor() {

    private val savedResource = MutableStateFlow(createInitial())

    suspend fun setNewValue(resource: ResourceDto) {
        savedResource.emit(resource)
    }

    suspend fun getCurrentValue() = savedResource.value

    fun observeChange(): Flow<ResourceDto> = savedResource.asStateFlow()


    private fun createInitial() = ResourceDto(value = 0.0)
}