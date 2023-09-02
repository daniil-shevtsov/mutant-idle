package com.daniil.shevtsov.idle.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.menu.domain.GetGameTitleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias ViewActionListener = (viewAction: ScreenViewAction) -> Unit


class ScreenHostViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
    private val getGameTitle: GetGameTitleUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(screenPresentationFunctionalCore(state = imperativeShell.getState()))
    val state = _state.asStateFlow()

    private var viewActionListener: ViewActionListener? = null

    private fun listenViewActions(listener: ViewActionListener) {
        viewActionListener = listener
    }

    init {
        listenViewActions(listener = { viewAction ->
            val (newState, effects) = screenFunctionalCore(
                state = imperativeShell.getState(),
                viewAction = viewAction,
            )
            println("action=$viewAction effects=$effects")

            imperativeShell.updateState(newState)

            handleEffects(effects)
        })
        viewActionListener?.invoke(ScreenViewAction.Start(GameStartViewAction.Init))

        imperativeShell.listen(listener = { newState ->
            _state.value = screenPresentationFunctionalCore(state = newState)
        })
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

        val action = GameStartViewAction.TitleReceived(title = titleFromServer)
        handleAction(gameStartAction(action))
    }

    private fun gameStartAction(action: GameStartViewAction) = ScreenViewAction.Start(action)

    fun handleAction(action: ScreenViewAction) {
        viewActionListener?.invoke(action)
    }

}
