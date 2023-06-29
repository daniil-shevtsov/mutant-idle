package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Jobs {

    val Mortician = playerJob(
        id = 0L,
        title = "Mortician",
        description = "Dissecting corpses",
        tags = listOf(
            Tags.Access.Morgue,
            Tags.Surgeon,
            Tags.SolitaryJob,
        )
    )

    val Gravedigger = playerJob(
        id = 1L,
        title = "Gravedigger",
        description = "Digging graves so you won't end up in one",
        tags = listOf(
            Tags.Access.Graveyard,
            Tags.LaborIntensive,
        )
    )

    val Butcher = playerJob(
        id = 2L,
        title = "Butcher",
        description = "Handling and selling meat",
        tags = listOf(
            Tags.Access.ButcherShop,
            Tags.SocialJob,
        )
    )

    val Unemployed = playerJob(
        id = 3L,
        title = "Unemployed",
        description = "I think you'll find what to do with your free time",
        tags = listOf(),
    )

    val ScrapyardMechanic = playerJob(
        id = 4L,
        title = "Scrapyard Mechanic",
        description = "Handling metal and parts",
        tags = listOf(
            Tags.Access.Scrapyard,
        )
    )

}


