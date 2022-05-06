package com.daniil.shevtsov.idle.feature.unlocks.domain

data class UnlockState(
    val species: Map<Long, Boolean>,
    val jobs: Map<Long, Boolean>,
)

fun unlockState(
    species: Map<Long, Boolean> = mapOf(),
    jobs: Map<Long, Boolean> = mapOf(),
) = UnlockState(
    species = species,
    jobs = jobs,
)
