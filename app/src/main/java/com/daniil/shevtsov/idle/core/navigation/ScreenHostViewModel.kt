package com.daniil.shevtsov.idle.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerViewState
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.player.species.domain.Species.Devourer
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class ScreenHostViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
) : ViewModel() {

    private val _state =
        MutableStateFlow(createInitialState())
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

    private fun createInitialState() = ScreenHostViewState(
        speciesId = Devourer.id,
        drawerState = drawerViewState(),
        contentState = ScreenContentViewState.Loading,
    )

}
