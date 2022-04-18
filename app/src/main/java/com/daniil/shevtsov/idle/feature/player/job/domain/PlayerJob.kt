package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerJob(
    val title: String,
    val tags: List<Tag>
)
