package com.daniil.shevtsov.idle.feature.player.core.domain


import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Player(
    val traits: Map<TraitId, PlayerTrait>,
    val generalTags: List<Tag>,
) {
    val tags: List<Tag>
        get() = generalTags + traits.values.map { it.tags }.flatten()
}
