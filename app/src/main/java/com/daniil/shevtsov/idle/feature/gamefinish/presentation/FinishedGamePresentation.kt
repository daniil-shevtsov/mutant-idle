package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import com.daniil.shevtsov.idle.feature.gamefinish.domain.FinishedGameState

fun mapFinishedGameViewState(
    state: FinishedGameState
): FinishedGameViewState {
    val ending = state.endings.firstOrNull() ?: return finishedGameViewState()

    return finishedGameViewState(
        endingState = EndingViewState(
            description = ending.description,
        ),
        unlocks = ending.unlocks.map { unlock ->
            with(unlock) {
                UnlockModel(
                    title = title,
                    subtitle = description,
                    unlockFeatures = unlock.newTags.map { tag ->
                        UnlockFeatureModel(
                            title = tag.name,
                            subtitle = "",
                        )
                    }
                )
            }
        }
    )
}
