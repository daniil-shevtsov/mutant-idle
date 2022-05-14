package com.daniil.shevtsov.idle.feature.unlocks.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

data class UnlockState(
    val traits: Map<TraitId, Map<Long, Boolean>>,
)

fun unlockState(
    traits: Map<TraitId, Map<Long, Boolean>> = mapOf(),
) = UnlockState(
    traits = traits,
)
