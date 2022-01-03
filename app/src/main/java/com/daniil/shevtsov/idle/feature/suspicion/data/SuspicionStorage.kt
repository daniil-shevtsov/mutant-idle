package com.daniil.shevtsov.idle.feature.suspicion.data

import com.daniil.shevtsov.idle.core.di.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class SuspicionStorage @Inject constructor() {

    private val savedRatio = MutableStateFlow(createInitial())

    suspend fun setNewValue(ratio: Double) {
        savedRatio.emit(ratio)
    }

    suspend fun getCurrentValue() = savedRatio.value

    fun observeChange(): Flow<Double> = savedRatio.asStateFlow()


    private fun createInitial() = 0.0
}