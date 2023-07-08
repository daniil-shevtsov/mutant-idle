package com.daniil.shevtsov.idle.feature.player.species.domain

import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.colors.presentation.DomainColor
import com.daniil.shevtsov.idle.feature.colors.presentation.SpeciesColors
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Species {
    val Devourer = playerSpecies(
        id = 0L,
        title = "Devourer",
        description = "You grow uncontrollably and suffer from insatiable hunger",
        icon = Icons.Devourer,
        tags = listOf(
            Tags.Species.Devourer,
            Tags.Access.Home,
        ),
        mainResource = ResourceKey.Blood,
        mainRatio = RatioKey.Mutanity,
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFD64747),
            background = DomainColor(0xFFAF0A0A),
            backgroundDark = DomainColor(0xFF750404),
            backgroundDarkest = DomainColor(0xFF2F1B1B),
            backgroundText = DomainColor(0xFFFFFFFF),
            textDark = DomainColor(0xFF000000),
            textLight = DomainColor(0xFFFFFFFF),
            iconLight = DomainColor(0xFFFFFFFF),
        )
    )

    val Shapeshifter = playerSpecies(
        id = 1L,
        title = "Shapeshifter",
        description = "You are a biomass which consumes people and changes shape",
        icon = Icons.Shapeshifter,
        mainResource = ResourceKey.DNA,
        tags = listOf(
            Tags.Species.ShapeShifter,
        ),
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFFFD8AB),
            background = DomainColor(0xFFD9A05F),
            backgroundDark = DomainColor(0xFF815D33),
            backgroundDarkest = DomainColor(0xFF4B2A1A),
            backgroundText = DomainColor(0xFFFAE0D0),
            textDark = DomainColor(0xFF1E0C0C),
            textLight = DomainColor(0xFFFFDEDE),
            iconLight = DomainColor(0xFFFFDEDE),
        )
    )

    val Vampire = playerSpecies(
        id = 2L,
        title = "Vampire",
        description = "You are a bloodsucking immortal creature",
        icon = Icons.Vampire,
        tags = listOf(
            Tags.Species.Vampire,
            Tags.Access.Home,
            Tags.Nature.Magic,
            Tags.Heliophobia,
            Tags.Immortal,
            Tags.Abilities.Hypnosis,
            Tags.PersonCapturer,
        ),
        mainResource = ResourceKey.Blood,
        mainRatio = RatioKey.Power,
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFAF3550),
            background = DomainColor(0xFF911A34),
            backgroundDark = DomainColor(0xFF600B1E),
            backgroundDarkest = DomainColor(0xFF2F000F),
            backgroundText = DomainColor(0xFFFACBE2),
            textDark = DomainColor(0xFF3D0D1C),
            textLight = DomainColor(0xFFfffeff),
            iconLight = DomainColor(0xFFfffeff),
        )
    )

    val Parasite = playerSpecies(
        id = 3L,
        title = "Parasite",
        description = "You live in people mind and can transfer from host to host",
        icon = Icons.Parasite,
        tags = listOf(
            Tags.Species.Parasite,
        ),
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFAB84FF),
            background = DomainColor(0xFF5F39B0),
            backgroundDark = DomainColor(0xFF41257D),
            backgroundDarkest = DomainColor(0xFF1A112D),
            backgroundText = DomainColor(0xFFE2D4FF),
            textDark = DomainColor(0xFF1E182A),
            textLight = DomainColor(0xFFDBCDFA),
            iconLight = DomainColor(0xFFDBCDFA),
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
        ),
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFD63421),
            background = DomainColor(0xFF841508),
            backgroundDark = DomainColor(0xFF53150E),
            backgroundDarkest = DomainColor(0xFF2F0C08),
            backgroundText = DomainColor(0xFFFFCBC2),
            textDark = DomainColor(0xFF360C08),
            textLight = DomainColor(0xFFFF1A00),
            iconLight = DomainColor(0xFFFF1A00),
        )
    )

    //TODO: What if in order to fix the ship you need to kidnap people to the crash site
    // and get DNA/blood/something else out of them
    val Alien = playerSpecies(
        id = 5L,
        title = "Alien",
        description = "You have crashed on this planet and need to find a way home",
        icon = Icons.Alien,
        tags = listOf(
            Tags.Species.Alien,
            Tags.Nature.Tech,
        ),
        mainResource = ResourceKey.Scrap,
        mainRatio = RatioKey.ShipRepair,
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFa0d29e),
            background = DomainColor(0xFF4F9766),
            backgroundDark = DomainColor(0xFF487463),
            backgroundDarkest = DomainColor(0xFF225149),
            backgroundText = DomainColor(0xFFE0FFF1),
            textDark = DomainColor(0xFF212B27),
            textLight = DomainColor(0xFFDFFCF1),
            iconLight = DomainColor(0xFFDFFCF1),
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
        ),
        mainResource = ResourceKey.Singularity,
        mainRatio = RatioKey.Suspicion,
        colors = SpeciesColors(
            backgroundLight = DomainColor(0xFFCDDEE5),
            background = DomainColor(0xFF94ACB6),
            backgroundDark = DomainColor(0xFF5A6E76),
            backgroundDarkest = DomainColor(0xFF36454B),
            backgroundText = DomainColor(0xFFFFFFFF),
            textDark = DomainColor(0xFF26235A),
            textLight = DomainColor(0xFFE0DEFB),
            iconLight = DomainColor(0xFFE0DEFB),
        )
    )
}
