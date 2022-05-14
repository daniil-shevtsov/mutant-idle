package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Species {
    val Devourer = playerSpecies(
        id = 0L,
        title = "Devourer",
        description = "You are growing uncontrollably and suffer from insatiable hunger",
        icon = Icons.Devourer,
        tags = listOf(
            Tags.Species.Devourer,
        ),

    )

    val Shapeshifter = playerSpecies(
        id = 1L,
        title = "Shapeshifter",
        description = "You are a mass of microorganism which consumes people and take on their appearance",
        icon = Icons.Shapeshifter,
        tags = listOf(
            Tags.Species.ShapeShifter,
        )
    )

    val Vampire = playerSpecies(
        id = 2L,
        title = "Vampire",
        description = "You are a bloodsucking immortal creature",
        icon = Icons.Vampire,
        tags = listOf(
            Tags.Species.Vampire,
            Tags.Nature.Magic,
            Tags.Heliophobia,
            Tags.Immortal,
            Tags.Hypnosis,
        )
    )

    val Parasite = playerSpecies(
        id = 3L,
        title = "Parasite",
        description = "You live in people mind and can transfer from host to host",
        icon = Icons.Parasite,
        tags = listOf(
            Tags.Species.Parasite,
        )
    )

    val Demon = playerSpecies(
        id = 4L,
        title = "Demon",
        description = "You posses meek mortals in order to complete your dark goals",
        icon = Icons.Demon,
        tags = listOf(
            Tags.Species.Demon,
            Tags.Nature.Magic,
        )
    )

    val Alien = playerSpecies(
        id = 5L,
        title = "Alien",
        description = "You have crashed on this planet and need to find a way home",
        icon = Icons.Alien,
        tags = listOf(
            Tags.Species.Alien,
            Tags.Nature.Tech,
        )
    )

    val Android = playerSpecies(
        id = 6L,
        title = "Android",
        description = "You have escaped from the lab and people are looking for you, luckily, you are almost indistinguishable from regular human",
        icon = Icons.Android,
        tags = listOf(
            Tags.Species.Android,
            Tags.Nature.Tech,
        )
    )
}
