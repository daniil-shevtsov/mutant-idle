package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.humanityRatioStub
import com.daniil.shevtsov.idle.core.ui.widgets.MyProgressBar
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel

@Preview
@Composable
fun MainPreview() {
    MutantRatioPane(model = humanityRatioStub())
}


@Composable
fun MutantRatioPane(
    model: HumanityRatioModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Pallete.Red)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = modifier,
            text = model.name,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        MyProgressBar(
            progressPercentage = model.percent.toFloat(),
            modifier = modifier,
        )
    }
}

