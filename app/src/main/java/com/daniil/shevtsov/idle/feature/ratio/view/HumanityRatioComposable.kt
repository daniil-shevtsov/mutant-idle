package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.humanityRatioStub
import com.daniil.shevtsov.idle.core.ui.protrusive
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Pallete.Red)
            .padding(4.dp),
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
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

@Composable
private fun MyProgressBar(
    progressPercentage: Float,
    modifier: Modifier = Modifier,
) {
    Box {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp)
                .cavitary(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Color.White),
        )
        Box(
            modifier = modifier
                .fillMaxWidth(fraction = progressPercentage)
                .height(40.dp)
                .padding(4.dp)
                .protrusive(
                    lightColor = Pallete.LightRed,
                    darkColor = Pallete.DarkRed
                )
                .background(Pallete.Red),
        )
    }
}