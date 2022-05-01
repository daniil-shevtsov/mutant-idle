package com.daniil.shevtsov.idle.feature.gamefinish.presentation

data class FinishedGameViewState(
    val endingState: EndingViewState,
    val unlocks: List<UnlockModel>
)
