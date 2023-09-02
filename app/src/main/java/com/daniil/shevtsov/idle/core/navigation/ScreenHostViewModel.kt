package com.daniil.shevtsov.idle.core.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.menu.domain.GetGameTitleUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias ViewActionListener = (viewAction: ScreenViewAction) -> Unit

class TitleEffectHandler(
    private val scope: CoroutineScope,
    private val getGameTitle: GetGameTitleUseCase,
) {
    private var viewActionListener: ViewActionListener? = null

    fun listenViewActions(listener: ViewActionListener) {
        viewActionListener = listener
    }

    private var requestTitleJob: Job? = null

    fun handleEffect(effect: MishaEffect) {
        when (effect) {
            MishaEffect.RequestTitleFromServer -> requestTitleFromServer()
            MishaEffect.CancelRequestingTitle -> cancelRequestingTitle()
        }
    }

    private fun cancelRequestingTitle() {
        println("KEK before cancelled job")
        requestTitleJob?.cancel()
        println("KEK cancelled job")
    }

    private fun requestTitleFromServer() {
        requestTitleJob = scope.launch {
            val titleFromServer = getGameTitle()

            val action = GameStartViewAction.TitleReceived(title = titleFromServer)
            viewActionListener?.invoke(gameStartAction(action))
        }
    }

    private fun gameStartAction(action: GameStartViewAction) = ScreenViewAction.Start(action)

}

class ScreenHostViewModel @Inject constructor(
    private val imperativeShell: MainImperativeShell,
    private val getGameTitle: GetGameTitleUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(screenPresentationFunctionalCore(state = imperativeShell.getState()))
    val state = _state.asStateFlow()

    private var viewActionListener: ViewActionListener? = null

    private val titleEffectHandler = TitleEffectHandler(
        scope = viewModelScope,
        getGameTitle = getGameTitle,
    )

    private fun listenViewActions(listener: ViewActionListener) {
        viewActionListener = listener
    }

    init {
        titleEffectHandler.listenViewActions(listener = { action ->
            handleAction(action)
        })

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
        effects.forEach { effect ->
            titleEffectHandler.handleEffect(effect)
        }
    }


    fun handleAction(action: ScreenViewAction) {
        viewActionListener?.invoke(action)
    }

}
