package com.daniil.shevtsov.idle.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.daniil.shevtsov.idle.core.ui.theme.AppColors
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.theme.alienColors
import com.daniil.shevtsov.idle.core.ui.theme.devourerColors
import com.daniil.shevtsov.idle.feature.gamefinish.view.FinishedGameScreen
import com.daniil.shevtsov.idle.feature.gamestart.view.GameStartScreen
import com.daniil.shevtsov.idle.feature.main.MainDrawer
import com.daniil.shevtsov.idle.feature.main.view.MainScreen
import com.daniil.shevtsov.idle.feature.player.species.domain.Species

@Composable
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel,
    modifier: Modifier = Modifier,
) {
    val delegatedViewState by viewModel.state.collectAsState()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }

    AppTheme(colors = chooseColorsForId(delegatedViewState.speciesId)) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        ModalDrawer(
            drawerState = drawerState,
            drawerContent = {
                MainDrawer(
                    drawerState = delegatedViewState.drawerState,
                    modifier = modifier,
                    onViewAction = { action -> viewModel.handleAction(ScreenViewAction.Drawer(action)) })
            },
            content = {
                when (val contentViewState = delegatedViewState.contentState) {
                    is ScreenContentViewState.GameStart -> {
                        GameStartScreen(
                            state = contentViewState.state,
                            modifier = modifier,
                            onAction = { action ->
                                viewModel.handleAction(
                                    ScreenViewAction.Start(
                                        action
                                    )
                                )
                            }
                        )
                    }
                    is ScreenContentViewState.Main -> {
                        MainScreen(
                            state = contentViewState.state,
                            modifier = modifier,
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
}

fun chooseColorsForId(speciesId: Long): AppColors {
    return when (speciesId) {
        Species.Devourer.id -> devourerColors()
        Species.Alien.id -> alienColors()
        else -> devourerColors()
    }
}
