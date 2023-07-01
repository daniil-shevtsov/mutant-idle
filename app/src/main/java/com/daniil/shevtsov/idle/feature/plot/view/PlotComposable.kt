package com.daniil.shevtsov.idle.feature.plot.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.plot.domain.plotEntry

@Preview
@Composable
fun PlotComposablePreview() {
    PlotComposable(
        plotEntries = plotPreviewData()
    )
}

@Composable
fun PlotComposable(
    plotEntries: List<PlotEntry>,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = modifier
            .background(AppTheme.colors.backgroundText)
            .padding(AppTheme.dimensions.paddingSmall),
        state = listState,
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
                Text(text = plotEntry.text, style = AppTheme.typography.body, color = textColor)
            }
        }
    }
    LaunchedEffect(plotEntries) {
        if (plotEntries.isNotEmpty()) {
            listState.animateScrollToItem(index = plotEntries.size - 1)
        }
    }
}

fun plotPreviewData() = listOf(
    plotEntry(
        text = """Today was a lol, and lol and loo, and finally lol.
                |Then there was gonna be a cheburek.
                |
                |Well, I didn't expect so much keks""".trimMargin()
    ),
    plotEntry(text = "Tomorrow there is gonna be kek"),
    plotEntry(text = "And finally Cheburek"),
)
