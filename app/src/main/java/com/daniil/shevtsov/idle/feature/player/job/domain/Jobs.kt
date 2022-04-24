package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.*

object Jobs {
    val Mortician = PlayerJob(
        id = 0L,
        title = "Mortician",
        tags = listOf(
            Surgeon,
            SolitaryJob,
            FreshCorpseAccess,
            IncineratorAccess,
        )
    )

    val Undertaker = PlayerJob(
        id = 1L,
        title = "Undertaker",
        tags = listOf(
            CorpseAccess,
            GraveyardAccess,
            LaborIntensive,
        )
    )

    val Butcher = PlayerJob(
        id = 2L,
        title = "Butcher",
        tags = listOf(
            MeatAccess,
            SocialJob,
        )
    )
}


