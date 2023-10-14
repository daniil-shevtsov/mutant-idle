package com.daniil.shevtsov.idle.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.upgradeListPreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.core.ui.widgets.CollapseButton
import com.daniil.shevtsov.idle.feature.action.view.ActionSection
import com.daniil.shevtsov.idle.feature.action.view.actionComposeStub
import com.daniil.shevtsov.idle.feature.location.view.LocationSelection
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.actionPane
import com.daniil.shevtsov.idle.feature.main.presentation.actionsState
import com.daniil.shevtsov.idle.feature.main.presentation.mainViewState
import com.daniil.shevtsov.idle.feature.main.presentation.upgradeViewState
import com.daniil.shevtsov.idle.feature.plot.domain.plotEntry
import com.daniil.shevtsov.idle.feature.plot.view.PlotComposable
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel
import com.daniil.shevtsov.idle.feature.resource.domain.resourceModel
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.upgrade.view.UpgradeList
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

@Preview(
    widthDp = 320,
    heightDp = 480,
)
@Composable
fun MainPreview() {
    MainScreen(
        state = mainViewState(
            plotEntries = listOf(
                plotEntry("lol kek cheburek (x10)"),
                plotEntry("lol kek cheburek (x10)"),
                plotEntry("lol kek cheburek (x10)"),
                plotEntry("lol kek cheburek (x10)"),
                plotEntry("lol kek cheburek (x10)"),
            ),
            resources = listOf(
                resourceModel(name = "Blood", value = "10 000", icon = Icons.Blood),
                resourceModel(name = "Money", value = "100", icon = Icons.Money),
                resourceModel(name = "Prisoners", value = "1", icon = Icons.Prisoner),
                resourceModel(name = "Information", value = "50", icon = Icons.Information),
                resourceModel(name = "Fresh Meat", value = "100", icon = Icons.FreshMeat),
                resourceModel(name = "Familiars", value = "150", icon = Icons.Familiar),
                resourceModel(name = "Organs", value = "10", icon = Icons.Organs),
            ),
            ratios = listOf(
                ratioModel(
                    title = "Suspicion",
                    name = "Investigation",
                    percent = 0.35,
                    icon = Icons.Suspicion
                ),
                ratioModel(
                    title = "Mutanity",
                    name = "Covert",
                    percent = 0.75,
                    icon = Icons.Mutanity
                ),
            ),
            actionState = actionsState(
                listOf(
                    actionPane(
                        listOf(
                            actionComposeStub(),
                            actionComposeStub(),
                            actionComposeStub()
                        )
                    )
                )
            ),
            upgradeState = upgradeViewState(
                upgradeListPreviewStub()
            ),
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

    //TODO: Make this behave based on upgrade collapse state
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = modifier
                    .height(500.dp)
                    .background(AppTheme.colors.backgroundLight)
                    .padding(top = 2.dp)
                    .background(AppTheme.colors.background)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.paddingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AppTheme.colors.background)
                        .padding(vertical = 4.dp)

                ) {
                    CollapseButton(
                        isCollapsed = bottomSheetScaffoldState.bottomSheetState.isExpanded,
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
                        style = AppTheme.typography.title,
                        color = AppTheme.colors.textLight,
                    )
                }
                Cavity(
                    mainColor = AppTheme.colors.background,
                ) {
                    UpgradeList(
                        upgradeList = state.shop.upgrades,
                        onUpgradeSelected = { id -> onViewAction(MainViewAction.SelectableClicked(id)) },
                        modifier = Modifier.fillMaxHeight()
                    )
                }
            }
        },
        sheetPeekHeight = 56.dp
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(AppTheme.colors.background)
                .padding(
                    start = AppTheme.dimensions.paddingM,
                    end = AppTheme.dimensions.paddingM,
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.Top,
        ) {
            Spacer(
                Modifier
                    .statusBarsHeight()
                    .fillMaxWidth()
            )
            Spacer(modifier.height(AppTheme.dimensions.paddingSmall))
            PlotComposable(
                plotEntries = state.plotEntries,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
            Spacer(modifier.height(AppTheme.dimensions.paddingSmall))
            LocationSelection(
                state = state.locationSelectionViewState,
                onExpandChange = { onViewAction(MainViewAction.LocationSelectionExpandChange) },
                onLocationSelected = { id -> onViewAction(MainViewAction.SelectableClicked(id)) },
                slotBeforeLocation = {
                    TurnCounter(
                        turnCount = state.turnCount,
                        modifier = Modifier
                    )
                    Text(
                        modifier = Modifier,
                        text = "Location:",
                        style = AppTheme.typography.title,
                        color = AppTheme.colors.textLight
                    )
                }
            )
            Spacer(modifier.size(AppTheme.dimensions.paddingSmall))
            ResourcePane(
                ratios = state.ratios,
                resources = state.resources,
                isCollapsed = state.sectionCollapse[SectionKey.Resources] ?: false,
                onToggleCollapse = {
                    onViewAction(
                        MainViewAction.ToggleSectionCollapse(
                            SectionKey.Resources
                        )
                    )
                },
            )
            Spacer(modifier.height(AppTheme.dimensions.paddingSmall))
            Cavity(
                mainColor = AppTheme.colors.background,
                modifier = Modifier
                    .weight(1f)
                    .background(AppTheme.colors.background),
            ) {
                ActionSection(
                    state = state.actionState,
                    isCollapsed = false,
                    onToggleCollapse = {
                        onViewAction(
                            MainViewAction.ToggleSectionCollapse(
                                SectionKey.Actions
                            )
                        )
                    },
                    onActionClicked = { id -> onViewAction(MainViewAction.SelectableClicked(id)) },
                )
            }
        }
    }
}

@Composable
private fun TurnCounter(modifier: Modifier, turnCount: Int) {
    Text(
        modifier = modifier,
        text = "Turn: $turnCount",
        style = AppTheme.typography.title,
        color = AppTheme.colors.textLight
    )
}
