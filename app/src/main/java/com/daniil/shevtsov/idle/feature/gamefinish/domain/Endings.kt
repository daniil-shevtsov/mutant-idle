package com.daniil.shevtsov.idle.feature.gamefinish.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.Jobs
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun createEndings() = listOf(
    ending(
        id = 0L,
        title = "Captured by the government",
        description = "They won't let you see sunlight again",
        unlocks = listOf(
            Species.Vampire,
            Jobs.Mortician,
        ).map(PlayerTrait::toUnlock)
    ),
    ending(
        id = 1L,
        title = "You have grown to an unstoppable mass",
        description = "Nothing on Earth can hurt you, so the only way for humanity is to submit",
        unlocks = listOf(
            Species.Shapeshifter,
            Jobs.Gravedigger,
        ).map(PlayerTrait::toUnlock)
    )
)

private fun PlayerTrait.toUnlock(): Unlock = unlock(
    title = "New ${
        when (traitId) {
            TraitId.Species -> "species"
            TraitId.Job -> "job"
        }
    }: $title",
    description = description,
    newTags = tags,
)
