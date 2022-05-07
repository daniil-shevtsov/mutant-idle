package com.daniil.shevtsov.idle.feature.debug.presentation

sealed class DebugViewAction {
    object UnlockEverything : DebugViewAction()
    data class JobSelected(val id: Long): DebugViewAction()
    data class SpeciesSelected(val id: Long) : DebugViewAction()
}
