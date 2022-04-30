package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.domain.MainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state = MutableStateFlow<MainViewState>(MainViewState.Loading)
    val state = _state.asStateFlow()

    private val temporaryHackyActionFlow =
        MutableSharedFlow<MainViewAction>()

    init {
        temporaryHackyActionFlow
            .onEach { viewAction ->
                val newFunctionalCoreState = mainFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction
                )

                updateImperativeShell(newState = newFunctionalCoreState)
            }
            .launchIn(viewModelScope)


        combine(
            imperativeShell.observeState(),
            temporaryHackyActionFlow.onStart { emit(MainViewAction.Init) }
        ) {state, viewAction ->
            mainPresentationFunctionalCore(
                state = state,
                viewAction = viewAction
            )
        }.onEach { viewState ->
            _state.value = viewState
        }
            .launchIn(viewModelScope)

    }

    fun handleAction(action: MainViewAction) {
        viewModelScope.launch {
            temporaryHackyActionFlow.emit(action)
        }
    }

    private fun updateImperativeShell(newState: MainFunctionalCoreState) {
        imperativeShell.updateState(newState)
    }

}
