package com.daniil.shevtsov.idle.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DrawerValue
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.navigation.ScreenHostViewModel
import com.daniil.shevtsov.idle.core.navigation.ScreenViewAction
import com.daniil.shevtsov.idle.core.navigation.ScreenViewState
import com.daniil.shevtsov.idle.core.ui.*
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.feature.action.view.ActionSection
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugComposable
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.idle.feature.drawer.view.DrawerTabSelector
import com.daniil.shevtsov.idle.feature.location.view.LocationSelection
import com.daniil.shevtsov.idle.feature.main.presentation.*
import com.daniil.shevtsov.idle.feature.player.info.view.PlayerInfoComposable
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJobModel
import com.daniil.shevtsov.idle.feature.ratio.view.MutantRatioPane
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.shop.view.Shop
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.google.accompanist.insets.statusBarsHeight

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainDrawerDebugPreview() {
    MainDrawer(
        state = mainViewState(
            drawerState = drawerViewState(
                tabSelectorState = listOf(
                    drawerTab(
                        id = DrawerTabId.Debug,
                        title = "Debug",
                        isSelected = true
                    ),
                    drawerTab(
                        id = DrawerTabId.PlayerInfo,
                        title = "Player Info",
                        isSelected = false
                    ),
                ),
                drawerContent = drawerDebugContent(
                    jobSelection = listOf(
                        playerJobModel(
                            title = "Lol",
                            tags = listOf(
                                tag(name = "lol1"),
                                tag(name = "lol2"),
                                tag(name = "lol3"),
                            )
                        ),
                        playerJobModel(
                            title = "Kek",
                            tags = listOf(
                                tag(name = "kek1"),
                                tag(name = "kek2"),
                                tag(name = "kek3"),
                            )
                        ),
                    )
                )
            )
        ),
        onViewAction = {},
    )
}

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    MainContent(
        state = mainViewState(
            resources = resourceStubs(),
            ratios = ratiosStubs(),
            actionState = actionStatePreviewStub(),
            shop = shopStatePreviewStub(),
            sectionCollapse = mapOf(
                SectionKey.Resources to false,
                SectionKey.Actions to false,
                SectionKey.Upgrades to false,
            ),
            drawerState = DrawerViewState(
                tabSelectorState = emptyList(),
                drawerContent = DrawerContentViewState.Debug(
                    state = debugViewState()
                )
            ),
        ),
        onViewAction = {},
    )
}

@Composable
fun MainScreen(
    viewModel: ScreenHostViewModel
) {
    val delegatedViewState by viewModel.state.collectAsState()

    when (val viewState = delegatedViewState) {
        is ScreenViewState.Main -> {
            MainContent(
                state = viewState.state,
                onViewAction = { action -> viewModel.handleAction(ScreenViewAction.Main(action)) },
            )
        }
    }
}

@Composable
fun MainContent(
    state: MainViewState,
    onViewAction: (MainViewAction) -> Unit,
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
            onViewAction = onViewAction,
        )
    }

}

@Composable
fun LoadingContent() {
    Text("Loading")
}

@Composable
fun SuccessContent(
    state: MainViewState.Success,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = { MainDrawer(state, onViewAction) },
        content = {
            ContentBody(
                state = state,
                onViewAction = onViewAction,
            )
        }
    )
}

@Composable
private fun MainDrawer(
    state: MainViewState.Success,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    DrawerTabSelector(
        tabs = state.drawerState.tabSelectorState,
        onTabSelected = { id -> onViewAction(MainViewAction.DrawerTabSwitched(id = id)) },
    )
    when (val drawerContentState = state.drawerState.drawerContent) {
        is DrawerContentViewState.Debug -> {
            DebugComposable(
                state = drawerContentState.state,
                modifier = modifier
                    .background(Pallete.Red)
                    .padding(8.dp),
                onAction = { action ->
                    val mainViewAction = when (action) {
                        is DebugViewAction.JobSelected -> MainViewAction.DebugJobSelected(action.id)
                        is DebugViewAction.SpeciesSelected -> MainViewAction.DebugSpeciesSelected(
                            action.id
                        )
                    }
                    onViewAction(mainViewAction)
                })
        }
        is DrawerContentViewState.PlayerInfo -> {
            PlayerInfoComposable(
                state = drawerContentState.playerInfo,
                modifier = modifier
                    .background(Pallete.Red)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
fun ContentBody(
    state: MainViewState.Success,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Pallete.Red),
        verticalArrangement = Arrangement.Top,
    ) {
        Spacer(
            modifier
                .statusBarsHeight()
                .fillMaxWidth()
        )
        LocationSelection(
            state = state.locationSelectionViewState,
            onExpandChange = { onViewAction(MainViewAction.LocationSelectionExpandChange) },
            onLocationSelected = { id -> onViewAction(MainViewAction.LocationSelected(id)) },
        )
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ResourcePane(
                resources = state.resources,
                isCollapsed = state.sectionCollapse[SectionKey.Resources] ?: false,
                modifier = modifier,
                onToggleCollapse = { onViewAction(MainViewAction.ToggleSectionCollapse(SectionKey.Resources)) },
            )
            MutantRatioPane(state.ratios, modifier = modifier)
        }

        Column(
            modifier = modifier
                .weight(1f)
                .background(Pallete.Red)
                .padding(4.dp)
        ) {
            fun hackyWeight(
                isCollapsed: Boolean
            ): Modifier {
                return if (isCollapsed) {
                    modifier
                } else {
                    modifier.weight(0.5f, fill = false)
                }
            }

            val isActionsCollapsed = state.sectionCollapse[SectionKey.Actions] ?: false
            Cavity(
                mainColor = Pallete.Red,
                modifier = hackyWeight(isCollapsed = isActionsCollapsed),
            ) {
                ActionSection(
                    state = state.actionState,
                    isCollapsed = isActionsCollapsed,
                    modifier = modifier,
                    onToggleCollapse = { MainViewAction.ToggleSectionCollapse(SectionKey.Actions) },
                    onActionClicked = { id -> MainViewAction.ActionClicked(id) },
                )
            }
            Spacer(
                modifier
                    .height(8.dp)
                    .fillMaxWidth()
            )
            val isShopCollapsed = state.sectionCollapse[SectionKey.Upgrades] ?: false
            Cavity(
                mainColor = Pallete.Red,
                modifier = hackyWeight(isCollapsed = isShopCollapsed)
            ) {
                Shop(
                    shop = state.shop,
                    isCollapsed = isShopCollapsed,
                    modifier = modifier,
                    onToggleCollapse = { MainViewAction.ToggleSectionCollapse(SectionKey.Upgrades) },
                    onUpgradeSelected = { id -> MainViewAction.UpgradeSelected(id) },
                )
            }
        }
    }
}

