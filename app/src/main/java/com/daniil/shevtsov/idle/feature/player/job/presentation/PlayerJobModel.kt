package com.daniil.shevtsov.idle.feature.player.job.presentation

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerJobModel(
    val id: Long,
    val title: String,
    val tags: List<Tag>,
    val isSelected: Boolean,
)
