package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Player(
    val job: PlayerJob,
    val species: PlayerSpecies,
    val generalTags: List<Tag>,
) {
    val tags: List<Tag>
        get() = generalTags + job.tags + species.tags
}
