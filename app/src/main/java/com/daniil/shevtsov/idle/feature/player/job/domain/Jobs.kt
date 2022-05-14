package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.toPlayerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Jobs {

    val Mortician = playerJob(
        id = 0L,
        title = "Mortician",
        tags = listOf(
            Tags.Access.Morgue,
            Tags.Surgeon,
            Tags.SolitaryJob,
        )
    )

    val Undertaker = playerJob(
        id = 1L,
        title = "Undertaker",
        tags = listOf(
            Tags.Access.Graveyard,
            Tags.LaborIntensive,
        )
    )

    val Butcher = playerJob(
        id = 2L,
        title = "Butcher",
        tags = listOf(
            Tags.Access.ButcherShop,
            Tags.SocialJob,
        )
    )

    val Unemployed = playerJob(
        id = 3L,
        title = "Unemployed",
        description = "You don't have a job",
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


