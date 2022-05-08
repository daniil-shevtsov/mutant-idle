package com.daniil.shevtsov.idle.feature.drawer.domain

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.main.domain.handleDrawerTabSwitched
import com.daniil.shevtsov.idle.feature.player.core.domain.PlayerViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.playerFunctionalCore
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

fun drawerFunctionalCore(
    state: GameState,
    viewAction: DrawerViewAction
): GameState {
    return when (viewAction) {
        is DrawerViewAction.TabSwitched -> handleDrawerTabSwitched(
            state = state,
            viewAction = viewAction,
        )
        is DrawerViewAction.Debug -> when (val debugAction = viewAction.action) {
            is DebugViewAction.JobSelected -> handleDebugJobSelected(
                state = state,
                viewAction = debugAction,
            )
            is DebugViewAction.TraitSelected -> playerFunctionalCore(
                state = state,
                action = PlayerViewAction.ChangeTrait(
                    traitId = debugAction.traitId,
                    id = debugAction.id,
                )
            )
            is DebugViewAction.SpeciesSelected -> handleSpeciesSelected(
                state = state,
                viewAction = debugAction,
            )
            is DebugViewAction.UnlockEverything -> unlockEverything(
                state = state,
                viewAction = debugAction,
            )
        }
    }
}

fun handleDebugJobSelected(
    state: GameState,
    viewAction: DebugViewAction.JobSelected
): GameState {
    val newTrait =
        state.availableTraits.find { it.traitId == TraitId.Job && it.id == viewAction.id }!!
    val newTraits = state.player.traits.toMutableMap().apply { put(TraitId.Job, newTrait) }.toMap()

    return state.copy(
        player = state.player.copy(
            traits = newTraits,
        )
    ).let { state ->
        playerFunctionalCore(
            state = state,
            action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
        )
    }

}

private fun handleSpeciesSelected(
    state: GameState,
    viewAction: DebugViewAction.SpeciesSelected
): GameState {
    val newTrait =
        state.availableTraits.find { it.traitId == TraitId.Species && it.id == viewAction.id }!!
    val newTraits =
        state.player.traits.toMutableMap().apply { put(TraitId.Species, newTrait) }.toMap()

    return state.copy(
        player = state.player.copy(
            traits = newTraits,
        )
    ).let { state ->
        playerFunctionalCore(
            state = state,
            action = PlayerViewAction.ChangeTrait(traitId = newTrait.traitId, id = viewAction.id),
        )
    }
}

private fun unlockEverything(
    state: GameState,
    viewAction: DebugViewAction.UnlockEverything
) = state.copy(
    unlockState = state.unlockState.copy(
        species = state.availableSpecies.map { species -> species.id to true }.toMap(),
        jobs = state.availableJobs.map { job -> job.id to true }.toMap(),
        traits = mapOf(
            TraitId.Species to state.availableSpecies.map { species -> species.id to true }.toMap(),
            TraitId.Job to state.availableJobs.map { job -> job.id to true }.toMap(),
        )
    )
)
