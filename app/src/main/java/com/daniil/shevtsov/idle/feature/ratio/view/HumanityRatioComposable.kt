package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.humanityRatioStub
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
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
    TitleWithProgress(
        title = model.name,
        progress = model.percent.toFloat(),
        modifier = modifier,
    )
}

