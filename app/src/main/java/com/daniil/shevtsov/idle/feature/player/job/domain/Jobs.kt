package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags

object Jobs {
    val Mortician = PlayerJob(
        id = 0L,
        title = "Mortician",
        tags = listOf(
            Tags.Surgeon,
            Tags.SolitaryJob,
            Tags.FreshCorpseAccess,
            Tags.IncineratorAccess,
        )
    )

    val Undertaker = PlayerJob(
        id = 1L,
        title = "Undertaker",
        tags = listOf(
            Tags.CorpseAccess,
            Tags.GraveyardAccess,
            Tags.LaborIntensive,
        )
    )

    val Butcher = PlayerJob(
        id = 2L,
        title = "Butcher",
        tags = listOf(
            Tags.MeatAccess,
            Tags.SocialJob,
        )
    )
}


