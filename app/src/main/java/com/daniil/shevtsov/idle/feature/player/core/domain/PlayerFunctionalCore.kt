package com.daniil.shevtsov.idle.feature.player.core.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun playerFunctionalCore(
    state: GameState,
    action: PlayerViewAction,
): GameState = when (action) {
    is PlayerViewAction.ChangeTrait -> changeTrait(state, action)
}

fun changeTrait(
    state: GameState,
    action: PlayerViewAction.ChangeTrait
): GameState {
    return state.copy(
        player = state.player.copy(
            traits = state.player.traits.toMutableMap().apply {
                val newTrait =
                    state.availableTraits.find { trait -> trait.traitId == action.traitId && trait.id == action.id }
                if (newTrait != null) {
                    put(action.traitId, newTrait)
                }
            }
                .toMap()
        )
    )
}
