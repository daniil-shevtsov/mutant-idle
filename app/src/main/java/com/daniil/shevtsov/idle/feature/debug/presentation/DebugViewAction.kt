package com.daniil.shevtsov.idle.feature.debug.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

sealed class DebugViewAction {
    object UnlockEverything : DebugViewAction()
    data class TraitSelected(val traitId: TraitId, val id: Long) : DebugViewAction()
    data class JobSelected(val id: Long): DebugViewAction()
    data class SpeciesSelected(val id: Long) : DebugViewAction()
}
