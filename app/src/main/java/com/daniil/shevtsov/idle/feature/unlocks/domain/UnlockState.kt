package com.daniil.shevtsov.idle.feature.unlocks.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

data class UnlockState(
    var species: Map<Long, Boolean> = mapOf(),
    val traits: Map<TraitId, Map<Long, Boolean>>,
) {
    init {
        val speciesTraits = traits[TraitId.Species]
        if(speciesTraits != null) {
            species = speciesTraits
        }
    }
}

fun unlockState(
    traits: Map<TraitId, Map<Long, Boolean>> = mapOf(),
) = UnlockState(
    traits = traits,
)
