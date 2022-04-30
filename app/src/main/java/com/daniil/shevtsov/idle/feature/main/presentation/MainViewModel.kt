package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val state = _state.asStateFlow()

    private val viewActionFlow = MutableSharedFlow<MainViewAction>()

    init {
        viewActionFlow
            .onStart { emit(MainViewAction.Init) }
            .onEach { viewAction ->
                val newState = mainFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction,
                )
                imperativeShell.updateState(newState)

                _state.value = mainPresentationFunctionalCore(state = newState)
            }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: MainViewAction) {
        viewModelScope.launch {
            viewActionFlow.emit(action)
        }
    }

}
