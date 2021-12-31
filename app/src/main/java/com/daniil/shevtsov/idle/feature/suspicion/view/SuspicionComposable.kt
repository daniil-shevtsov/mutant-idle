package com.daniil.shevtsov.idle.feature.suspicion.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.suspicionStub
import com.daniil.shevtsov.idle.core.ui.widgets.MyProgressBar
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
    MyProgressBar(
        progressPercentage = suspicion.percent.toFloat(),
        modifier = modifier.fillMaxWidth()
    )
}