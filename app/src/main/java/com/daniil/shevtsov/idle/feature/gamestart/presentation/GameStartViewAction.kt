package com.daniil.shevtsov.idle.feature.gamestart.presentation

sealed class GameStartViewAction {
    data class SpeciesSelected(val id: Long) : GameStartViewAction()
    data class JobSelected(val id: Long) : GameStartViewAction()
    object StartGame : GameStartViewAction()
}
