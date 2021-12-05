package com.daniil.shevtsov.idle.main.data.resource

import com.daniil.shevtsov.idle.core.di.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class ResourceStorage @Inject constructor() {

    private val savedResource = MutableStateFlow(createInitial())

    suspend fun setNewValue(resource: Double) {
        savedResource.emit(resource)
    }

    suspend fun getCurrentValue() = savedResource.value

    fun observeChange(): Flow<Double> = savedResource.asStateFlow()


    private fun createInitial() = 0.0
}