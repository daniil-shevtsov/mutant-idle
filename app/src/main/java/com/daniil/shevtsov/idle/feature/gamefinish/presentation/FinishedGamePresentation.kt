package com.daniil.shevtsov.idle.feature.gamefinish.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState

fun mapFinishedGameViewState(
    state: GameState
): FinishedGameViewState {
    val ending = state.allEndings.find { it.id == state.currentEndingId }!!

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
                            subtitle = tag.description.orEmpty(),
                        )
                    }
                )
            }
        }
    )
}
