package com.daniil.shevtsov.idle.feature.debug.presentation

sealed class DebugViewAction {
    data class JobSelected(val id: Long): DebugViewAction()
}
