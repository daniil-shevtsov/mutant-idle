package com.daniil.shevtsov.idle.feature.debug.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class DebugConfigStorage @Inject constructor() {

    private val jobs =  MutableStateFlow(emptyList<PlayerJob>())

    fun addAvailableJobs(availableJobs: List<PlayerJob>) {
        jobs.value = availableJobs
    }

    fun observeAll(): Flow<List<PlayerJob>> {
        return jobs.asStateFlow()
    }
}
