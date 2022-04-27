package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun player(
    job: PlayerJob = playerJob(),
    tags: List<Tag> = emptyList(),
) = Player(
    job = job,
    tags = tags,
)
