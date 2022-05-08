package com.daniil.shevtsov.idle.feature.player.info.presentation

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class PlayerInfoState(
    val playerTraits: List<PlayerTrait>,
    val playerTags: List<Tag>,
)
