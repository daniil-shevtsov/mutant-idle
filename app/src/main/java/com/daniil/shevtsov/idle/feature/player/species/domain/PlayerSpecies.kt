package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerSpecies(
    val id: Long,
    val title: String,
    val tags: List<Tag>
)
