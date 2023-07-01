package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme


@Preview
@Composable
fun MainPreview() {
    Column {
        listOf("Kek", "Covert", "Investigation").forEach { name ->
            listOf(0.25f, 0.5f, 0.6f, 0.75f).forEach { percentage ->
                TitleWithProgress(
                    title = "Lol",
                    name = name,
                    progress = percentage,
                    icon = Icons.Mutanity,
                )
            }
        }
    }
}

@Composable
fun TitleWithProgress(
    title: String,
    name: String,
    progress: Float,
    icon: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingSmall),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = icon,
            style = AppTheme.typography.icon,
            color = AppTheme.colors.textLight
        )
        //TODO: Replace with composable lambda
        if (title.isNotEmpty()) {
            Text(
                modifier = Modifier.weight(0.35f),
                text = title,
                maxLines = 1,
                style = AppTheme.typography.title,
                color = AppTheme.colors.textLight,
                textAlign = TextAlign.Start
            )
        }
        Box(
            modifier = Modifier.weight(0.65f),
            contentAlignment = Alignment.Center,
        ) {
            MyProgressBar(
                progressPercentage = progress,
            )
            Text(
                text = name,
                maxLines = 1,
                style = AppTheme.typography.body,
                color = when {
                    progress >= 0.65f -> AppTheme.colors.textLight
                    else -> AppTheme.colors.textDark
                }
            )
        }
    }
}
