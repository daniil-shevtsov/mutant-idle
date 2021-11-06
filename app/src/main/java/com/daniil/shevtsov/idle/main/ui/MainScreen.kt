package com.daniil.shevtsov.idle.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.main.AndroidMainViewModel
import com.daniil.shevtsov.idle.main.ui.resource.ResourcePanel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeList

@Preview(
    widthDp = 320,
    heightDp = 534,
)
@Composable
fun MainPreview() {
    val state = viewStatePreview()
    MainContent(state = state)
}

@Composable
fun MainScreen(
    viewModel: AndroidMainViewModel
) {
    val viewState by viewModel.state.collectAsState()
    MainContent(state = viewState)
}

@Composable
fun MainContent(
    state: MainViewState
) {
    when (state) {
        is MainViewState.Loading -> LoadingContent()
        is MainViewState.Success -> SuccessContent(state)
    }

}

@Composable
fun LoadingContent() {
    Text("Loading")
}

@Composable
fun SuccessContent(state: MainViewState.Success) {
    Column(
        modifier = Modifier
            .background(Pallete.DarkGray)
    ) {
        ResourcePanel(state.resource)
        UpgradeList(
            upgradeList = state.upgrades,
            modifier = Modifier.fillMaxHeight()
        )
    }
}