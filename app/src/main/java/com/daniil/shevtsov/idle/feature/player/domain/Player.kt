package com.daniil.shevtsov.idle.feature.player.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Player(
    val tags: List<Tag>,
)