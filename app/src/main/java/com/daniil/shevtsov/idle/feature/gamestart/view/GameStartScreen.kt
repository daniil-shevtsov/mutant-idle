package com.daniil.shevtsov.idle.feature.gamestart.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.Pallete
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.gameStartViewState

@Preview
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
    Column(modifier = modifier.background(Pallete.Red)) {
        Text(text = state.title)
        Text(text = state.description)
        SpeciesSelection(
            selection = state.speciesSelection,
            onItemClicked = { id -> onAction(GameStartViewAction.SpeciesSelected(id)) }
        )
    }
}
