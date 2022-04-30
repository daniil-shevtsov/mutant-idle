package com.daniil.shevtsov.idle.feature.gamefinish.presentation

fun finishedGameViewState(
    endingState: EndingViewState = endingViewState(),
    unlocks: List<UnlockModel> = emptyList(),
) = FinishedGameViewState(
    endingState = endingState,
    unlocks = unlocks,
)

fun endingViewState(
    description: String = "",
) = EndingViewState(
    description = description,
)
