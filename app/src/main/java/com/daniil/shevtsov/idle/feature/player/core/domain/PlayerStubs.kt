package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.job.domain.PlayerJob
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tag

fun player(
    job: PlayerJob = playerJob(),
    species: PlayerSpecies = playerSpecies(),
    generalTags: List<Tag> = emptyList(),
    traits: Map<TraitId, PlayerTrait> = mapOf(),
) = Player(
    job = job,
    species = species,
    traits = traits,
    generalTags = generalTags,
)
