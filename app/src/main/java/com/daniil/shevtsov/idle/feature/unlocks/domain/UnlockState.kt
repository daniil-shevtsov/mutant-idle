package com.daniil.shevtsov.idle.feature.unlocks.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

data class UnlockState(
    var species: Map<Long, Boolean>,
    var jobs: Map<Long, Boolean>,
    val traits: Map<TraitId, Map<Long, Boolean>>,
) {
    init {
        val speciesTraits = traits[TraitId.Species]
        val jobTraits = traits[TraitId.Job]
        if(speciesTraits != null) {
            species = speciesTraits
        }
        if(jobTraits != null) {
            jobs = jobTraits
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
