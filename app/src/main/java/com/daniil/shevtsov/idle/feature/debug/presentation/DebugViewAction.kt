package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob

sealed class DebugViewAction {
    data class JobSelected(val job: PlayerJob): DebugViewAction()
}