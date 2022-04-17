package com.daniil.shevtsov.idle.feature.playerjob.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerJob(
    val tags: List<Tag>
)