package com.daniil.shevtsov.idle.feature.tagsystem.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.createDefaultTags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.game
import com.daniil.shevtsov.idle.feature.tagsystem.domain.entity.withAdditional
import com.daniil.shevtsov.idle.feature.tagsystem.presentation.GameViewState
import com.daniil.shevtsov.idle.feature.tagsystem.presentation.gamePresentation
import com.daniil.shevtsov.idle.feature.tagsystem.presentation.presentPlot

@Preview
@Composable
fun GameSpikeScreenPreview() {
    GameSpikeScreen()
}

@Composable
fun GameSpikeScreen(modifier: Modifier = Modifier) {
    var currentGame by remember {
        mutableStateOf(
            gamePresentation(
                game(
                    tags = createDefaultTags().withAdditional("ability" to "flight")
                )
            )
        )
    }

    GameView(currentGame)
}

@Composable
fun GameView(
    viewState: GameViewState,
    modifier: Modifier = Modifier,
) {
    Text(
        text = presentPlot(viewState.game.plot).joinToString(separator = "\n"),
        modifier = Modifier.background(Color.White).padding(4.dp)
            .fillMaxWidth()
            .height(100.dp)
            .verticalScroll(rememberScrollState())
    )
}

@Composable
private fun Title(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        modifier = modifier,
        style = AppTheme.typography.bodyTitle,
        color = AppTheme.colors.textLight
    )
}

@Composable
private fun Tag(
    text: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Text(
        text,
        modifier = modifier
            .let {
                when {
                    isSelected -> it
                        .background(AppTheme.colors.backgroundLight)
                        .padding(2.dp)

                    else -> it.padding(2.dp)
                }
            }
            .background(AppTheme.colors.background),
        style = AppTheme.typography.body,
        color = AppTheme.colors.textLight
    )
}
