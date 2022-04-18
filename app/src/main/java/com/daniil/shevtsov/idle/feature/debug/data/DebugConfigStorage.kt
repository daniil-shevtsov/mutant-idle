package com.daniil.shevtsov.idle.feature.debug.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import javax.inject.Inject

@AppScope
class DebugConfigStorage @Inject constructor() {
    fun addAvailableJobs(availableJobs: List<PlayerJob>) {

    }
}
