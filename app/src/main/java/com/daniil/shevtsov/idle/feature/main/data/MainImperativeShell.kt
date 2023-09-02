package com.daniil.shevtsov.idle.feature.main.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import javax.inject.Inject

typealias GameStateListener = (state: GameState) -> Unit

@AppScope
class MainImperativeShell @Inject constructor() {

    private var listener: GameStateListener? = null
    private var lastState: GameState = createInitialGameState()

    fun listen(listener: GameStateListener) {
        this.listener = listener
    }

    fun getState(): GameState {
        return lastState
    }

    fun updateState(newState: GameState) {
        listener?.invoke(newState)
        lastState = newState
    }

}
