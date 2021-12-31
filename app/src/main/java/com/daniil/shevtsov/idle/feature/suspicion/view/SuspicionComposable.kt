package com.daniil.shevtsov.idle.feature.suspicion.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.suspicionStub
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.suspicion.presentation.SuspicionModel

@Preview
@Composable
fun SuspicionPanelPreview() {
    SuspicionPanel(suspicion = suspicionStub())
}

@Composable
fun SuspicionPanel(
    suspicion: SuspicionModel,
    modifier: Modifier = Modifier,
) {
    TitleWithProgress(
        title = suspicion.title,
        progress = suspicion.percent.toFloat(),
        modifier = modifier,
    )
}