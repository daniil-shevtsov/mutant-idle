package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun player(
    generalTags: List<Tag> = emptyList(),
    traits: Map<TraitId, PlayerTrait> = mapOf(),
) = Player(
    traits = traits,
    generalTags = generalTags,
)
