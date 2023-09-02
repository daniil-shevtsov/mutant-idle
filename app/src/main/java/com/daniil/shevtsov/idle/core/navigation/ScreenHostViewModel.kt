package com.daniil.shevtsov.idle.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.menu.domain.GetGameTitleUseCase
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
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
    private val getGameTitle: GetGameTitleUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(screenPresentationFunctionalCore(state = imperativeShell.getState()))
    val state = _state.asStateFlow()

    private val viewActionFlow = MutableSharedFlow<ScreenViewAction>()

    init {
        viewActionFlow
            .onStart { emit(ScreenViewAction.Start(GameStartViewAction.Init)) }
            .onEach { viewAction ->
                val (newState, effects) = screenFunctionalCore(
                    state = imperativeShell.getState(),
                    viewAction = viewAction,
                )
                println("action=$viewAction effects=$effects")

                imperativeShell.updateState(newState)

                handleEffects(effects)
            }
            .launchIn(viewModelScope)

        imperativeShell.observeState()
            .onEach { state ->
                _state.value = screenPresentationFunctionalCore(state = state)
            }
            .launchIn(viewModelScope)
    }

    private fun handleEffects(effects: List<MishaEffect>) {
        viewModelScope.launch {
            effects.forEach { effect ->
                when (effect) {
                    MishaEffect.RequestTitleFromServer -> requestTitleFromServer()
                }
            }
        }
    }

    private suspend fun requestTitleFromServer() {
        val titleFromServer = getGameTitle()
        val currentState = imperativeShell.getState()
        imperativeShell.updateState(
            currentState.copy(
                gameTitle = MenuTitleState.Result(titleFromServer)
            )
        )
    }

    fun handleAction(action: ScreenViewAction) {
        viewModelScope.launch {
            viewActionFlow.emit(action)
        }
    }

}
