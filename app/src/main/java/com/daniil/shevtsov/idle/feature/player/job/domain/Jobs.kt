package com.daniil.shevtsov.idle.feature.player.job.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.tagsystem.domain.*

val Mortician = PlayerJob(
    title = "Mortician",
    tags = listOf(
        Surgeon,
        SolitaryJob,
        FreshCorpseAccess,
        IncineratorAccess,
    )
)

val Undertaker = PlayerJob(
    title = "Undertaker",
    tags = listOf(
        CorpseAccess,
        GraveyardAccess,
        LaborIntensive,
    )
)

val Butcher = PlayerJob(
    title = "Butcher",
    tags = listOf(
        MeatAccess,
        SocialJob,
    )
)
