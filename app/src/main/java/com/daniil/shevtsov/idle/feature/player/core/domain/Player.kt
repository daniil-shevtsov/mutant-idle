package com.daniil.shevtsov.idle.feature.player.core.domain


import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

data class Player(
    val traits: Map<TraitId, PlayerTrait>,
    val generalTags: List<Tag>,
) {
    val tags: List<Tag>
        get() = generalTags + traits.values.map { it.tags }.flatten()

    val mainResourceKey: ResourceKey
        get() = traits[TraitId.Species]?.mainResource ?: ResourceKey.Blood
    val mainRatioKey: RatioKey
        get() = traits[TraitId.Species]?.mainRatio ?: RatioKey.Mutanity
}
