package com.daniil.shevtsov.idle.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.MainViewAction
import com.daniil.shevtsov.idle.main.MainViewModel
import com.daniil.shevtsov.idle.main.ui.resource.ResourcePanel
import com.daniil.shevtsov.idle.main.ui.shop.Shop

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
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(
            state = state,
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
    onUpgradeSelected: (upgradeId: Long) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(Pallete.DarkGray)
    ) {
        ResourcePanel(state.resource)
        Shop(state.shop, onUpgradeSelected)
    }
}