package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

sealed class PlayerViewAction {
    data class ChangeTrait(val traitId: TraitId, val id: Long) : PlayerViewAction()
}
