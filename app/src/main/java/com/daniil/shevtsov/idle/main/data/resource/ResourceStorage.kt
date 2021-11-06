package com.daniil.shevtsov.idle.main.data.resource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class ResourceStorage @Inject constructor() {

    private val savedResource = MutableStateFlow(createInitial())

    fun setNewValue(resource: ResourceDto) {
        savedResource.value = resource
    }

    fun observeChange(): Flow<ResourceDto> = savedResource.asStateFlow()


    private fun createInitial() = ResourceDto(value = 0L)
}