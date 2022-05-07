package com.daniil.shevtsov.idle.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.daniil.shevtsov.idle.feature.gamefinish.view.FinishedGameScreen
import com.daniil.shevtsov.idle.feature.gamestart.view.GameStartScreen
import com.daniil.shevtsov.idle.feature.main.view.MainDrawer
import com.daniil.shevtsov.idle.feature.main.view.MainScreen

@Composable
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel
) {
    val delegatedViewState by viewModel.state.collectAsState()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            MainDrawer(
                delegatedViewState.drawerState,
                { action -> viewModel.handleAction(ScreenViewAction.Drawer(action)) })
        },
        content = {
            when (val contentViewState = delegatedViewState.contentState) {
                is ScreenContentViewState.GameStart -> {
                    GameStartScreen(
                        state = contentViewState.state,
                        onAction = { action -> viewModel.handleAction(ScreenViewAction.Start(action)) }
                    )
                }
                is ScreenContentViewState.Main -> {
                    MainScreen(
                        state = contentViewState.state,
                        onViewAction = { action ->
                            viewModel.handleAction(
                                ScreenViewAction.Main(
                                    action
                                )
                            )
                        },
                    )
                }
                is ScreenContentViewState.FinishedGame -> {
                    FinishedGameScreen(
                        state = contentViewState.state,
                    )
                }
            }
        }
    )

}
