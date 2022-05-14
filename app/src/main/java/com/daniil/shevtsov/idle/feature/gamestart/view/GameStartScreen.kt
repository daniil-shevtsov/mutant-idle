package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.gameStartViewState
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId

@Preview(
    widthDp = 230,
    heightDp = 534,
)
@Composable
fun GameStartScreenPreview() {
    GameStartScreen(
        state = gameStartViewState(
            title = "MUTANT IDLE",
            description = "Choose Species",
            speciesSelection = speciesSelectionComposeStub(),
        ),
        onAction = {},
    )
}

@Composable
fun GameStartScreen(
    state: GameStartViewState,
    onAction: (GameStartViewAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .background(AppTheme.colors.background)
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Text(
            text = state.title,
            style = AppTheme.typography.header,
            color = AppTheme.colors.textLight,
        )
        Text(
            text = state.description,
            style = AppTheme.typography.title,
            color = AppTheme.colors.textLight,
        )
        SpeciesSelection(
            selection = state.speciesSelection,
            onItemClicked = { id ->
                onAction(
                    GameStartViewAction.TraitSelected(
                        traitId = TraitId.Species,
                        id = id,
                    )
                )
            }
        )
        SpeciesSelection(
            selection = state.jobSelection,
            onItemClicked = { id -> onAction(GameStartViewAction.TraitSelected(TraitId.Job, id)) }
        )
        Box(
            modifier = modifier
                .cavitary(
                    lightColor = AppTheme.colors.backgroundLight,
                    darkColor = AppTheme.colors.backgroundDark,
                )
                .background(AppTheme.colors.backgroundDarkest)
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = modifier
                    .clickable {
                        onAction(GameStartViewAction.StartGame)
                    }
                    .background(AppTheme.colors.background)
                    .protrusive(
                        lightColor = AppTheme.colors.backgroundLight,
                        darkColor = AppTheme.colors.backgroundDark,
                    )
                    .background(AppTheme.colors.background)
                    .padding(4.dp)
                    .fillMaxWidth(),
                text = "Start Game",
                textAlign = TextAlign.Center,
                style = AppTheme.typography.button,
                color = AppTheme.colors.textLight,
            )
        }
    }
}
