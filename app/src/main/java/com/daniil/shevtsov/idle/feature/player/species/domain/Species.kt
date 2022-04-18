package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.*

val Blob = PlayerSpecies(
    tags = listOf(
        Devourer,
        Mutating,
        Growth,
    )
)

val Vampire = PlayerSpecies(
    tags = listOf(
        Heliophobia,
        Immortal,
        Hypnosis,
    )
)
