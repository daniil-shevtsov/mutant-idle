package com.daniil.shevtsov.idle.feature.ratio.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.widgets.TitleWithProgress
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.ratio.presentation.ratioModel

@Preview
@Composable
fun RatioPanePreview() {
    RatioPane(
        ratios = listOf(
            ratioModel(title = "Mutanity", name = "Covert", percent = 0.75, percentLabel = "75 %"),
            ratioModel(
                title = "Suspicion",
                name = "Investigation",
                percent = 0.35,
                percentLabel = "35 %"
            ),
        )
    )
}


@Composable
fun RatioPane(
    ratios: List<RatioModel>,
    modifier: Modifier = Modifier,
) {
    Column {
        ratios.forEach { model ->
            TitleWithProgress(
                title = model.title,
                name = model.percentLabel + " " + model.name,
                progress = model.percent.toFloat(),
                modifier = modifier,
            )
        }
    }
}

