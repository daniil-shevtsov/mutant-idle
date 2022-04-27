package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Player(
    val job: PlayerJob,
    val tags: List<Tag>,
)
