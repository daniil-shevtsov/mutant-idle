package com.daniil.shevtsov.idle.feature.main.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.viewStatePreviewStub
import com.daniil.shevtsov.idle.feature.action.view.ActionSection
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewModel
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewState
import com.daniil.shevtsov.idle.feature.ratio.view.MutantRatioPane
import com.daniil.shevtsov.idle.feature.resource.view.ResourcePane
import com.daniil.shevtsov.idle.feature.shop.view.Shop
import com.google.accompanist.insets.statusBarsHeight

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    val state = viewStatePreviewStub()
    MainContent(state = state)
}

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val viewState by viewModel.state.collectAsState()
    MainContent(
        state = viewState,
        onUpgradeSelected = { upgradeId ->
            viewModel.handleAction(
                MainViewAction.UpgradeSelected(
                    upgradeId
                )
            )
        }
    )
}

@Composable
fun MainContent(
    state: MainViewState,
    onActionClicked: (actionId: Long) -> Unit = {},
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
            onActionClicked = onActionClicked,
            onUpgradeSelected = onUpgradeSelected,
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
    onActionClicked: (actionId: Long) -> Unit = {},
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(Pallete.Red),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        Spacer(
            Modifier
                .statusBarsHeight()
                .fillMaxWidth()
        )
        ResourcePane(state.resources)
        MutantRatioPane(state.ratio)
        ActionSection(
            state = state.actionState,
            onActionClicked = onActionClicked,
            modifier = Modifier.weight(0.5f),
        )
        Shop(
            shop = state.shop,
            onUpgradeSelected = onUpgradeSelected,
            modifier = Modifier.weight(0.5f),
        )
    }
}