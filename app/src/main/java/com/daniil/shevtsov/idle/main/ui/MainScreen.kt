package com.daniil.shevtsov.idle.main.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.daniil.shevtsov.idle.main.AndroidMainViewModel

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
    Resource(state.resource)
}

@Composable
fun Resource(
    model: ResourceModel,
) {
    Text(model.text)
}