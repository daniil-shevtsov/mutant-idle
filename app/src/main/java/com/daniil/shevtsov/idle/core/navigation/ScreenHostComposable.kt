package com.daniil.shevtsov.idle.core.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.theme.chooseThemeForId
import com.daniil.shevtsov.idle.feature.colors.presentation.DomainColor
import com.daniil.shevtsov.idle.feature.colors.presentation.SpeciesColors
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerContentViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewState
import com.daniil.shevtsov.idle.feature.gamefinish.view.FinishedGameScreen
import com.daniil.shevtsov.idle.feature.gamestart.view.GameStartScreen
import com.daniil.shevtsov.idle.feature.main.MainDrawer
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.view.MainScreen
import com.daniil.shevtsov.idle.feature.menu.view.MenuScreen
import com.daniil.shevtsov.idle.feature.settings.view.SettingsScreen
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ScreenHostComposable(
    viewModel: ScreenHostViewModel,
    modifier: Modifier = Modifier,
) {
    val emptyValue = ScreenHostViewState(
        speciesId = 3241, colors = SpeciesColors(
            backgroundLight = DomainColor(argb = 4610),
            background = DomainColor(argb = 2255),
            backgroundDark = DomainColor(argb = 5868),
            backgroundDarkest = DomainColor(
                argb = 5139
            ),
            backgroundText = DomainColor(argb = 6685),
            textDark = DomainColor(argb = 6056),
            textLight = DomainColor(argb = 5943),
            iconLight = DomainColor(argb = 7747)
        ), drawerState = DrawerViewState(
            tabSelectorState = listOf(),
            drawerContent = DrawerContentViewState.Debug(
                DebugViewState(
                    jobSelection = listOf(),
                    speciesSelection = listOf(),
                    traitSelections = listOf()
                )
            )
        ), contentState = ScreenContentViewState.Loading
    )
    val delegatedViewState by viewModel.state.collectAsState(emptyValue)

    val systemUiController = rememberSystemUiController()

    BackHandler {
        viewModel.handleAction(ScreenViewAction.General(GeneralViewAction.Back))
    }
    val theme = chooseThemeForId(delegatedViewState.speciesId, delegatedViewState.colors)
    AppTheme(
        colors = theme.colors,
        shapes = theme.shapes,
    ) {
        val toolbarColor = AppTheme.colors.background
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = toolbarColor,
            )
        }

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
                    is ScreenContentViewState.Menu -> {
//                        SpikeTagScreen(modifier = modifier.fillMaxSize())
                        MenuScreen(
                            state = contentViewState.state,
                            onClick = { menuId ->
                                viewModel.handleAction(
                                    ScreenViewAction.Main(
                                        MainViewAction.MenuButtonClicked(menuId)
                                    )
                                )
                            },
                            modifier = modifier,
                        )
                    }

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
                            modifier = modifier,
                            onAction = { action ->
                                viewModel.handleAction(ScreenViewAction.Main(action))
                            }
                        )
                    }

                    is ScreenContentViewState.Settings -> SettingsScreen(
                        state = contentViewState.state,
                        onAction = { action ->
                            viewModel.handleAction(ScreenViewAction.Main(action))
                        },
                        modifier = modifier,
                    )

                    ScreenContentViewState.Loading -> Unit
                }
            }
        )
    }
}
