package com.daniil.shevtsov.idle.main.data.time

import com.daniil.shevtsov.idle.core.di.AppScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import kotlin.time.Duration

@AppScope
class TimeStorage @Inject constructor() {

    private val savedTime = MutableStateFlow(createInitial())

    suspend fun setNewValue(value: Duration) {
        savedTime.emit(value)
    }

    suspend fun getCurrentValue() = savedTime.value

    fun observeChange(): Flow<Duration> = savedTime.asStateFlow()


    private fun createInitial() = Duration.ZERO

}