package com.daniil.shevtsov.idle.feature.tagsystem.presentation

import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.Game

fun gamePresentation(game: Game): GameViewState {
    return GameViewState(game)
}
