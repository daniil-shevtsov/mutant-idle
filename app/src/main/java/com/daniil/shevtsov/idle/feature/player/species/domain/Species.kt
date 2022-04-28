package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Species {
    val Devourer = playerSpecies(
        id = 0L,
        title = "Devourer",
        tags = listOf(
            Tags.Species.Devourer,
            Tags.PersonCapturer,
        )
    )

    val Shapeshifter = playerSpecies(
        id = 1L,
        title = "Shapeshifter",
        tags = listOf(
            Tags.Species.ShapeShifter,
        )
    )

    val Vampire = playerSpecies(
        id = 2L,
        title = "Vampire",
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
        tags = listOf(
            Tags.Species.Parasite,
        )
    )

    val Demon = playerSpecies(
        id = 4L,
        title = "Demon",
        tags = listOf(
            Tags.Species.Demon,
            Tags.Nature.Magic,
        )
    )

    val Alien = playerSpecies(
        id = 5L,
        title = "Alien",
        tags = listOf(
            Tags.Species.Alien,
            Tags.Nature.Tech,
        )
    )

    val Android = playerSpecies(
        id = 6L,
        title = "Android",
        tags = listOf(
            Tags.Species.Android,
            Tags.Nature.Tech,
        )
    )
}
