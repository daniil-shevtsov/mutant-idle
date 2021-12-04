package com.daniil.shevtsov.idle.main.data.time

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class TimeStorage @Inject constructor() {

    private val savedTime = MutableStateFlow(createInitial())

    suspend fun setNewValue(resource: Double) {
        savedTime.emit(resource)
    }

    suspend fun getCurrentValue() = savedTime.value

    fun observeChange(): Flow<Double> = savedTime.asStateFlow()


    private fun createInitial() = 0.0

}