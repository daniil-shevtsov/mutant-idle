package com.daniil.shevtsov.idle.feature.suspicion.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    SuspicionSlot(modifier = modifier) { modifier ->
        MyProgressBar(
            progressPercentage = suspicion.percent.toFloat(),
            modifier = modifier
        )
    }
}

@Composable
fun SuspicionSlot(
    modifier: Modifier = Modifier,
    content: @Composable (modifier: Modifier) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = modifier,
            text = "Free",
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        content(modifier.weight(1f))
        Text(
            modifier = modifier,
            text = "Found",
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}