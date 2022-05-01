package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.humanityRatioModel

@Preview
@Composable
fun MainPreview() {
    MutantRatioPane(ratios = listOf(
        humanityRatioModel(title = "Mutanity", name = "Covert", percent = 0.75),
        humanityRatioModel(title = "Suspicion", name = "Investigation", percent = 0.35),
    ))
}


@Composable
fun MutantRatioPane(
    ratios: List<HumanityRatioModel>,
    modifier: Modifier = Modifier,
) {
    Column {
        ratios.forEach { model ->
            TitleWithProgress(
                title = model.title,
                progress = model.percent.toFloat(),
                modifier = modifier,
            )
        }
    }
}

