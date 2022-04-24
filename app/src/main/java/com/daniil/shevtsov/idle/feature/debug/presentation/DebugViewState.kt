package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel

data class DebugViewState(
    val jobSelection: List<PlayerJobModel>,
)
