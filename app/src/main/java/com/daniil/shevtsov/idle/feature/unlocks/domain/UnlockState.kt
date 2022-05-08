package com.daniil.shevtsov.idle.feature.unlocks.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

data class UnlockState(
    var species: Map<Long, Boolean>,
    val jobs: Map<Long, Boolean>,
    val traits: Map<TraitId, Map<Long, Boolean>>,
) {
    init {
        species = when (traits.isNotEmpty()) {
            true -> traits[TraitId.Species]!!
            false -> species
        }
    }
}

fun unlockState(
    species: Map<Long, Boolean> = mapOf(),
    jobs: Map<Long, Boolean> = mapOf(),
    traits: Map<TraitId, Map<Long, Boolean>> = mapOf(),
) = UnlockState(
    species = species,
    jobs = jobs,
    traits = traits,
)
