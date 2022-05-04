package com.daniil.shevtsov.idle.feature.gamestart.presentation

sealed class GameStartViewAction {
    data class SpeciesSelected(val id: Long) : GameStartViewAction()
}
