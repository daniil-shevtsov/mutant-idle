package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerJob(
    val id: Long,
    val title: String,
    val tags: List<Tag>
)
