package com.daniil.shevtsov.idle.feature.unlocks.domain

data class UnlockState(
    val species: Map<Long, Boolean>,
)

fun unlockState(
    species: Map<Long, Boolean> = mapOf(),
) = UnlockState(
    species = species,
)
