package com.daniil.shevtsov.idle.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.daniil.shevtsov.idle.feature.gamefinish.view.FinishedGameScreen
import com.daniil.shevtsov.idle.feature.main.view.MainScreen

@Composable
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel
) {
    val delegatedViewState by viewModel.state.collectAsState()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }

    when (val viewState = delegatedViewState) {
        is ScreenViewState.Main -> {
            MainScreen(
                state = viewState.state,
                onViewAction = { action -> viewModel.handleAction(ScreenViewAction.Main(action)) },
            )
        }
        is ScreenViewState.FinishedGame -> {
            FinishedGameScreen(
                state = viewState.state,
            )
        }
    }
}
