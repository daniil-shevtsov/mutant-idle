package com.daniil.shevtsov.idle.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.viewStatePreviewStub
import com.daniil.shevtsov.idle.core.ui.widgets.Cavity
import com.daniil.shevtsov.idle.feature.action.view.ActionSection
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewModel
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.ratio.view.MutantRatioPane
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.shop.view.Shop
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    val state = viewStatePreviewStub()
    MainContent(
        state = state,
        onActionClicked = {},
        onUpgradeSelected = {},
        onToggleCollapse = {}
    )
}

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreviewAllCollapsed() {
    val state = viewStatePreviewStub().copy(
        sectionCollapse = mapOf(
            SectionKey.Resources to true,
            SectionKey.Actions to true,
            SectionKey.Upgrades to true,
        )
    )
    MainContent(
        state = state,
        onActionClicked = {},
        onUpgradeSelected = {},
        onToggleCollapse = {}
    )
}

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreviewActionsExpanded() {
    val state = viewStatePreviewStub().copy(
        sectionCollapse = mapOf(
            SectionKey.Resources to true,
            SectionKey.Actions to false,
            SectionKey.Upgrades to true,
        )
    )
    MainContent(
        state = state,
        onActionClicked = {},
        onUpgradeSelected = {},
        onToggleCollapse = {}
    )
}

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreviewUpgradesExpanded() {
    val state = viewStatePreviewStub().copy(
        sectionCollapse = mapOf(
            SectionKey.Resources to true,
            SectionKey.Actions to true,
            SectionKey.Upgrades to false,
        )
    )
    MainContent(
        state = state,
        onActionClicked = {},
        onUpgradeSelected = {},
        onToggleCollapse = {}
    )
}

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val viewState by viewModel.state.collectAsState()
    MainContent(
        state = viewState,
        onActionClicked = { actionId ->
            viewModel.handleAction(MainViewAction.ActionClicked(id = actionId))
        },
        onUpgradeSelected = { upgradeId ->
            viewModel.handleAction(MainViewAction.UpgradeSelected(id = upgradeId))
        },
        onToggleCollapse = { sectionKey ->
            viewModel.handleAction(MainViewAction.ToggleSectionCollapse(key = sectionKey))
        }
    )
}

@Composable
fun MainContent(
    state: MainViewState,
    onActionClicked: (actionId: Long) -> Unit,
    onUpgradeSelected: (upgradeId: Long) -> Unit,
    onToggleCollapse: (key: SectionKey) -> Unit,
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
            onActionClicked = onActionClicked,
            onUpgradeSelected = onUpgradeSelected,
            onToggleCollapse = onToggleCollapse
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
    modifier: Modifier = Modifier,
    onActionClicked: (actionId: Long) -> Unit = {},
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
    onToggleCollapse: (key: SectionKey) -> Unit,
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        drawerContent = {
            Column {
                Text("Text in Drawer")
                Button(onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                }) {
                    Text("Close Drawer")
                }
            }
        },
        content = {
            ContentBody(
                state = state,
                onActionClicked = onActionClicked,
                onUpgradeSelected = onUpgradeSelected,
                onToggleCollapse = onToggleCollapse
            )
        }
    )
}

@Composable
fun ContentBody(
    state: MainViewState.Success,
    modifier: Modifier = Modifier,
    onActionClicked: (actionId: Long) -> Unit = {},
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
    onToggleCollapse: (key: SectionKey) -> Unit,
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
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ResourcePane(
                resources = state.resources,
                isCollapsed = state.sectionCollapse[SectionKey.Resources] ?: false,
                modifier = modifier,
                onToggleCollapse = { onToggleCollapse(SectionKey.Resources) },
            )
            MutantRatioPane(state.ratios, modifier = modifier)
        }
        fun Modifier.hackyWeight(
            isCollapsed: Boolean
        ): Modifier {
            return if (isCollapsed) {
                modifier
            } else {
                modifier.weight(0.5f, fill = false)
            }
        }
        Column(
            modifier = modifier
                .weight(1f)
                .background(Pallete.Red)
                .padding(4.dp)
        ) {
            val isActionsCollapsed = state.sectionCollapse[SectionKey.Actions] ?: false
            Cavity(
                mainColor = Pallete.Red,
                modifier = modifier.hackyWeight(isCollapsed = isActionsCollapsed),
            ) {
                ActionSection(
                    state = state.actionState,
                    isCollapsed = isActionsCollapsed,
                    modifier = modifier,
                    onToggleCollapse = { onToggleCollapse(SectionKey.Actions) },
                    onActionClicked = onActionClicked,
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
                modifier = modifier.hackyWeight(isCollapsed = isShopCollapsed)
            ) {
                Shop(
                    shop = state.shop,
                    isCollapsed = isShopCollapsed,
                    modifier = modifier,
                    onToggleCollapse = { onToggleCollapse(SectionKey.Upgrades) },
                    onUpgradeSelected = onUpgradeSelected,
                )
            }
        }
    }
}

