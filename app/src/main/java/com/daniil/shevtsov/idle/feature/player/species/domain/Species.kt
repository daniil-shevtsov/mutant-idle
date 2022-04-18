package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Devourer
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Heliophobia
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Immortal
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Mutating

val Blob = PlayerSpecies(
    tags = listOf(
        Devourer,
        Mutating,
    )
)

val Vampire = PlayerSpecies(
    tags = listOf(
        Heliophobia,
        Immortal,
    )
)
