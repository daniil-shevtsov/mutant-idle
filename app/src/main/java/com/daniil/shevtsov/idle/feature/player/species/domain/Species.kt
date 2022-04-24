package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

val Blob = PlayerSpecies(
    tags = listOf(
        Tags.Devourer,
        Tags.Mutating,
        Tags.Growth,
    )
)

val Vampire = PlayerSpecies(
    tags = listOf(
        Tags.Heliophobia,
        Tags.Immortal,
        Tags.Hypnosis,
    )
)
