package com.daniil.shevtsov.idle.core.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
            .fillMaxWidth()
            .background(AppTheme.colors.background)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = icon, fontSize = 24.sp)
        Text(
            modifier = modifier.weight(0.35f),
            text = title,
            fontSize = 24.sp,
            color = Color.White,
            textAlign = TextAlign.Start
        )
        Box(
            modifier = modifier.weight(0.65f),
            contentAlignment = Alignment.Center,
        ) {
            MyProgressBar(
                progressPercentage = progress,
            )
            Text(
                text = name,
                color = when {
                    progress >= 0.65f -> Color.White
                    else -> Color.Black
                }
            )
        }
    }
}
