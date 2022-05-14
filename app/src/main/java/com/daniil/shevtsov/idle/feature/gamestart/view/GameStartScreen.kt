package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.core.ui.cavitary
import com.daniil.shevtsov.idle.core.ui.protrusive
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
            .background(Pallete.Background)
            .fillMaxHeight()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = spacedBy(8.dp, Alignment.CenterVertically),
    ) {
        Text(text = state.title, color = Color.White, fontSize = 32.sp)
        Text(text = state.description, color = Color.White, fontSize = 24.sp)
        SpeciesSelection(
            selection = state.speciesSelection,
            onItemClicked = { id -> onAction(GameStartViewAction.TraitSelected(TraitId.Species, id)) }
        )
        SpeciesSelection(
            selection = state.jobSelection,
            onItemClicked = { id -> onAction(GameStartViewAction.TraitSelected(TraitId.Job, id)) }
        )
        Box(
            modifier = modifier
                .cavitary(
                    lightColor = Pallete.BackgroundLight,
                    darkColor = Pallete.BackgroundDark,
                )
                .background(Pallete.BackgroundDarkest)
                .padding(2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                modifier = modifier
                    .clickable {
                        onAction(GameStartViewAction.StartGame)
                    }
                    .background(Pallete.Background)
                    .protrusive(
                        lightColor = Pallete.BackgroundLight,
                        darkColor = Pallete.BackgroundDark,
                    )
                    .background(Pallete.Background)
                    .padding(4.dp)
                    .fillMaxWidth(),
                text = "Start Game",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                color = Color.White,
            )
        }
    }
}
