package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.Jobs
import com.daniil.shevtsov.idle.feature.player.species.domain.Species

fun createEndings() = listOf(
    ending(
        id = 1L,
        title = "Captured by the government",
        description = "Captured by the government",
//        description = "They won't let you see sunlight again",
        unlocks = listOf(
            unlock(
                title = "New class: Vampire",
                description = "You are a bloodsucking immortal creature",
                newTags = Species.Vampire.tags,
            ),
            unlock(
                title = "New job: Mortician",
                description = "You work in a morgue",
                newTags = Jobs.Mortician.tags,
            )
        )
    )
)
