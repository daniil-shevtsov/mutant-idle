package com.daniil.shevtsov.idle.feature.debug.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class DebugConfigStorage @Inject constructor(
    private val initial: List<PlayerJob>
) {

    private val jobs =  MutableStateFlow(initial)

    fun addAvailableJobs(availableJobs: List<PlayerJob>) {
        jobs.value = availableJobs
    }

    fun observeAvailableJobs(): Flow<List<PlayerJob>> {
        return jobs.asStateFlow()
    }
}
