package com.daniil.shevtsov.idle.feature.time.data

import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.flow.Flow
import kotlin.time.Duration

interface TimeProvider {
    suspend fun startUpdatingItemWith(interval: Duration)
    fun observeTime(): Flow<Time>
}