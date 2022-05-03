package com.daniil.shevtsov.idle.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.debugViewState
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CollapseButton
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
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel
import com.daniil.shevtsov.idle.feature.ratio.view.RatioPane
import com.daniil.shevtsov.idle.feature.resource.domain.resourceModel
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.view.UpgradeList
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

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
    MainScreen(
        state = mainViewState(
            resources = listOf(
                resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
                resourceModel(name = "Money", value = "100", icon = Icons.Money),
            ),
            ratios = listOf(
                ratioModel(title = "Mutanity", name = "Covert", percent = 0.75),
                ratioModel(title = "Suspicion", name = "Investigation", percent = 0.35),
            ),
            actionState = actionsState(),
            upgradeState = upgradeViewState(),
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
fun LoadingContent() {
    Text("Loading")
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
    val scope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    )

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .height(500.dp)
                    .background(Pallete.LightRed)
                    .padding(top = 2.dp)
                    .background(Pallete.Red)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(Pallete.Red)
                        .padding(vertical = 4.dp)

                ) {
                    CollapseButton(
                        isCollapsed = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                        modifier = modifier,
                        onClick = {
                            scope.launch {
                                if(bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }
                    )
                    Text(
                        text = "Upgrades",
                        color = Color.White,
                        fontSize = 24.sp,
                        modifier = modifier
                    )
                }
                Cavity(
                    mainColor = Pallete.Red,
                    modifier = modifier,
                ) {
                    UpgradeList(
                        upgradeList = state.shop.upgrades,
                        onUpgradeSelected = { id -> onViewAction(MainViewAction.UpgradeSelected(id)) },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        },
        sheetPeekHeight = 56.dp
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
                    onToggleCollapse = {
                        onViewAction(
                            MainViewAction.ToggleSectionCollapse(
                                SectionKey.Resources
                            )
                        )
                    },
                )
                RatioPane(state.ratios, modifier = modifier)
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
                        onToggleCollapse = {
                            onViewAction(
                                MainViewAction.ToggleSectionCollapse(
                                    SectionKey.Actions
                                )
                            )
                        },
                        onActionClicked = { id -> onViewAction(MainViewAction.ActionClicked(id)) },
                    )
                }
            }
        }
    }
}
