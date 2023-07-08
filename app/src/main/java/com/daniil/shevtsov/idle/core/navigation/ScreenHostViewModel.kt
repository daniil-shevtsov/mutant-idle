package com.daniil.shevtsov.idle.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScreenHostViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state =
        MutableStateFlow(screenPresentationFunctionalCore(state = imperativeShell.getState()))
    val state = _state.asStateFlow()

    private val viewActionFlow = MutableSharedFlow<ScreenViewAction>()

    init {
        viewActionFlow
            .onStart { emit(ScreenViewAction.Main(MainViewAction.Init)) }
            .onEach { viewAction ->
                val newState = screenFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction,
                )

                imperativeShell.updateState(newState)
            }
            .launchIn(viewModelScope)

        imperativeShell.observeState()
            .onEach { state ->
                _state.value = screenPresentationFunctionalCore(state = state)
            }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: ScreenViewAction) {
        viewModelScope.launch {
            viewActionFlow.emit(action)
        }
    }

}
