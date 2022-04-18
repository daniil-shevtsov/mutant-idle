package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob

data class DebugViewState(
    val jobSelection: List<PlayerJob>,
)
