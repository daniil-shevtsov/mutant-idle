package com.daniil.shevtsov.idle.feature.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.core.navigation.ScreenViewAction
import com.daniil.shevtsov.idle.core.navigation.ScreenViewState
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state =
        MutableStateFlow<ScreenViewState>(ScreenViewState.Main(MainViewState.Loading))
    val state = _state.asStateFlow()

    private val viewActionFlow = MutableSharedFlow<ScreenViewAction>()

    init {
        viewActionFlow
            .onStart { emit(ScreenViewAction.Main(MainViewAction.Init)) }
            .onEach { viewAction ->
                when (viewAction) {
                    is ScreenViewAction.Main -> {
                        val newState = mainFunctionalCore(
                            state = imperativeShell.getState(),
                            viewAction = viewAction.action,
                        )
                        imperativeShell.updateState(newState)

                        _state.value =
                            ScreenViewState.Main(mainPresentationFunctionalCore(state = newState))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun handleAction(action: MainViewAction) {
        viewModelScope.launch {
            viewActionFlow.emit(ScreenViewAction.Main(action))
        }
    }

}
