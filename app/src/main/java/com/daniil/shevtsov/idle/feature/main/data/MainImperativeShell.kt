package com.daniil.shevtsov.idle.feature.main.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class MainImperativeShell @Inject constructor(
    initialState: GameState
) {

    private val state = MutableStateFlow(initialState)

    fun getState(): GameState = state.value

    fun updateState(newState: GameState) {
        state.value = newState
    }

    fun observeState(): Flow<GameState> = state.asStateFlow()

}
