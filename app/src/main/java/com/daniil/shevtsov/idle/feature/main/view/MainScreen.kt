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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.theme.alienColors
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CollapseButton
import com.daniil.shevtsov.idle.feature.action.view.ActionSection
import com.daniil.shevtsov.idle.feature.location.view.LocationSelection
import com.daniil.shevtsov.idle.feature.main.presentation.*
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel
import com.daniil.shevtsov.idle.feature.ratio.view.RatioPane
import com.daniil.shevtsov.idle.feature.resource.domain.resourceModel
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.upgrade.view.UpgradeList
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun AlienMainPreview() {
    AppTheme(colors = alienColors()) {
        MainScreen(
            state = mainViewState(
                resources = listOf(
                    resourceModel(name = "Scrap", value = "10 000", icon = Icons.Scrap),
                    resourceModel(name = "Money", value = "100", icon = Icons.Money),
                ),
                ratios = listOf(
                    ratioModel(title = "Ship Repair", icon = Icons.ShipRepair, percent = 0.75),
                    ratioModel(title = "Suspicion", name = "Investigation", percent = 0.35),
                ),
                actionState = actionsState(),
                upgradeState = upgradeViewState(),
                sectionCollapse = mapOf(
                    SectionKey.Resources to false,
                    SectionKey.Actions to false,
                    SectionKey.Upgrades to false,
                ),
            ),
            onViewAction = {},
        )
    }
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
        ),
        onViewAction = {},
    )
}

@Composable
fun MainScreen(
    state: MainViewState,
    modifier: Modifier = Modifier,
    onViewAction: (MainViewAction) -> Unit,
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
            onViewAction = onViewAction,
            modifier = modifier,
        )
    }

}

@Composable
fun SuccessContent(
    state: MainViewState.Success,
    onViewAction: (MainViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ContentBody(
        state = state,
        onViewAction = onViewAction,
        modifier = modifier,
    )
}

@Composable
fun LoadingContent() {
    Text("Loading")
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
                    .background(AppTheme.colors.backgroundLight)
                    .padding(top = 2.dp)
                    .background(AppTheme.colors.background)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.background)
                        .padding(vertical = 4.dp)

                ) {
                    CollapseButton(
                        isCollapsed = bottomSheetScaffoldState.bottomSheetState.isExpanded,
                        modifier = modifier,
                        onClick = {
                            scope.launch {
                                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
                                    bottomSheetScaffoldState.bottomSheetState.expand()
                                } else {
                                    bottomSheetScaffoldState.bottomSheetState.collapse()
                                }
                            }
                        }
                    )
                    Text(
                        text = "Upgrades",
                        color = AppTheme.colors.textLight,
                        fontSize = 24.sp,
                        modifier = modifier
                    )
                }
                Cavity(
                    mainColor = AppTheme.colors.background,
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
    ) { paddingValues ->
        Column(
            modifier = modifier
                .background(AppTheme.colors.background)
                .padding(bottom = paddingValues.calculateBottomPadding()),
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

            Cavity(
                mainColor = AppTheme.colors.background,
                modifier = modifier
                    .weight(1f)
                    .background(AppTheme.colors.background)
                    .padding(4.dp),
            ) {
                ActionSection(
                    state = state.actionState,
                    isCollapsed = false,
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
