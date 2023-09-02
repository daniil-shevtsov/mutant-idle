package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

sealed class GameStartViewAction {
    object RequestTitle : GameStartViewAction()
    data class TraitSelected(val traitId: TraitId, val id: Long) : GameStartViewAction()
    object StartGame : GameStartViewAction()
}
