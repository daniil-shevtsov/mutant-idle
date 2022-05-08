package com.daniil.shevtsov.idle.feature.player.core.domain

import assertk.Assert
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.player.species.domain.PlayerSpecies

fun Assert<Player>.assertSpeciesSelected(id: Long) = prop(Player::species)
    .prop(PlayerSpecies::id)
    .isEqualTo(id)
