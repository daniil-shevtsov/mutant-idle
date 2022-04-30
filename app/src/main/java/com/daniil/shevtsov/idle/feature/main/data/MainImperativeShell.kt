package com.daniil.shevtsov.idle.feature.main.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.domain.toMainState
import com.daniil.shevtsov.idle.feature.main.domain.updateGameState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class MainImperativeShell @Inject constructor(
    initialState: GameState
) {

    private val state = MutableStateFlow(initialState)

    fun getState(): MainFunctionalCoreState = state.value.toMainState()

    fun updateState(newState: MainFunctionalCoreState) {
        state.value = newState.updateGameState(state.value)
    }

    fun observeState(): Flow<GameState> = state.asStateFlow()

}
