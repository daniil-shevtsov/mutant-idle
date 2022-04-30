package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Jobs {

    val Mortician = PlayerJob(
        id = 0L,
        title = "Mortician",
        tags = listOf(
            Tags.Access.Morgue,
            Tags.Surgeon,
            Tags.SolitaryJob,
        )
    )

    val Undertaker = PlayerJob(
        id = 1L,
        title = "Undertaker",
        tags = listOf(
            Tags.Access.Graveyard,
            Tags.LaborIntensive,
        )
    )

    val Butcher = PlayerJob(
        id = 2L,
        title = "Butcher",
        tags = listOf(
            Tags.Access.ButcherShop,
            Tags.SocialJob,
        )
    )

    val Unemployed = PlayerJob(
        id = 3L,
        title = "Unemployed",
        tags = listOf(),
    )

    val ScrapyardMechanic = playerJob(
        id = 4L,
        title = "Scrapyard Mechanic",
        tags = listOf(
            Tags.Access.Scrapyard,
        )
    )

}


