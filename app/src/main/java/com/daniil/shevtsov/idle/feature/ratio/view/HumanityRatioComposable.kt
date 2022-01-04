package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.ratiosStubs
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel

@Preview
@Composable
fun MainPreview() {
    MutantRatioPane(ratios = ratiosStubs())
}


@Composable
fun MutantRatioPane(
    ratios: List<HumanityRatioModel>,
    modifier: Modifier = Modifier,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ratios.forEach { model ->
            TitleWithProgress(
                title = model.name,
                progress = model.percent.toFloat(),
                modifier = modifier,
            )
        }
    }
}

