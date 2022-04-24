package com.daniil.shevtsov.idle.feature.main.data

import com.daniil.shevtsov.idle.core.di.AppScope
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@AppScope
class MainImperativeShell @Inject constructor(
    initialState: MainFunctionalCoreState
) {

    private val state = MutableStateFlow(initialState)

    fun getState(): MainFunctionalCoreState = state.value

    fun updateState(newState: MainFunctionalCoreState) {
        state.value = newState
    }

    fun observeState(): Flow<MainFunctionalCoreState> = state.asStateFlow()

}