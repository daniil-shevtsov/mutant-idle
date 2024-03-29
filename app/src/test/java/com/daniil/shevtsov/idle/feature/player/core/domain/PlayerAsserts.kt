package com.daniil.shevtsov.idle.feature.player.core.domain

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.player.trait.domain.PlayerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun Assert<Player>.assertSpeciesSelected(id: Long) = prop(Player::traits)
    .prop(name = "speciesTrait", extract = { traits -> traits[TraitId.Species] })
    .isNotNull()
    .prop(PlayerTrait::id)
    .isEqualTo(id)

fun Assert<Player>.assertJobSelected(id: Long) = prop(Player::traits)
    .prop(name = "jobTrait", extract = { traits -> traits[TraitId.Job] })
    .isNotNull()
    .prop(PlayerTrait::id)
    .isEqualTo(id)
