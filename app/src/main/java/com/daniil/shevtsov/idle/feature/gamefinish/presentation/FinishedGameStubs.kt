package com.daniil.shevtsov.idle.feature.gamefinish.presentation

fun previewFinishedGameViewState(
    endingState: EndingViewState = endingViewState(),
    unlocks: List<UnlockModel> = emptyList(),
) = FinishedGameViewState(
    endingState = endingState,
    unlocks = unlocks,
)

fun endingViewState(
    title: String = "",
    description: String = "",
) = EndingViewState(
    title = title,
    description = description,
)
