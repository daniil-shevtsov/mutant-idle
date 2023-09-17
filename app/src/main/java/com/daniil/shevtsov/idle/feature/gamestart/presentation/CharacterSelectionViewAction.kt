package com.daniil.shevtsov.idle.feature.gamestart.presentation

import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

sealed class CharacterSelectionViewAction {
    object Init : CharacterSelectionViewAction()
    data class TitleReceived(val title: String) : CharacterSelectionViewAction()
    data class TraitSelected(val traitId: TraitId, val id: Long) : CharacterSelectionViewAction()
    object StartGame : CharacterSelectionViewAction()
}
