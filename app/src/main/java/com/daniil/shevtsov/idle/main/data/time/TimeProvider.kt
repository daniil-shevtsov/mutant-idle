package com.daniil.shevtsov.idle.main.data.time

import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface TimeProvider {
    suspend fun startUpdatingItemWith(interval: Duration)
    fun observeTime(): Flow<Time>
}