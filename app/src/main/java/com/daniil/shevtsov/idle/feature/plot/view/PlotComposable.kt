package com.daniil.shevtsov.idle.feature.plot.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme

@Preview
@Composable
fun PlotComposablePreview() {
    PlotComposable(
        plotEntries = plotPreviewData()
    )
}

@Composable
fun PlotComposable(
    plotEntries: List<String>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .padding(AppTheme.dimensions.paddingSmall)
            .background(AppTheme.colors.backgroundText),
        reverseLayout = true,
    ) {
        items(plotEntries) { plotEntry ->
            Row(horizontalArrangement = spacedBy(AppTheme.dimensions.paddingSmall)) {
                val textColor = AppTheme.colors.textDark
                Text(
                    text = "*",
                    style = AppTheme.typography.body,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Text(text = plotEntry, style = AppTheme.typography.body, color = textColor)
            }
        }
    }
}

fun plotPreviewData() = listOf(
    """Today was a lol, and lol and loo, and finally lol.
                |Then there was gonna be a cheburek.
                |
                |Well, I didn't expect so much keks""".trimMargin(),
    "Tomorrow there is gonna be kek",
    "And finally Cheburek",
)
