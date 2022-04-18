package com.daniil.shevtsov.idle.feature.playerjob.domain

import com.daniil.shevtsov.idle.feature.tagsystem.domain.*

val Mortician = PlayerJob(
    tags = listOf(
        Surgeon,
        SolitaryJob,
        FreshCorpseAccess,
        IncineratorAccess,
    )
)

val Undertaker = PlayerJob(
    tags = listOf(
        CorpseAccess,
        GraveyardAccess,
        LaborIntensive,
    )
)

val Butcher = PlayerJob(
    tags = listOf(
        MeatAccess,
        SocialJob,
    )
)
