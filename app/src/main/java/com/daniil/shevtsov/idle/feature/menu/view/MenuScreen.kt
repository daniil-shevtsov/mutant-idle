package com.daniil.shevtsov.idle.feature.menu.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuButtonModel
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun MenuScreenPreview() {
    MenuScreen(
        state = menuViewStateComposeStub()
    )
}

@Composable
fun MenuScreen(
    state: MenuViewState,
    modifier: Modifier = Modifier,
) {
    Column {
        Text(
            text = state.title,
            style = AppTheme.typography.title,
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            state.buttons.forEach { buttonModel ->
                MenuButton(model = buttonModel)
            }
        }
    }
}

@Composable
private fun MenuButton(
    model: MenuButtonModel,
    modifier: Modifier = Modifier,
) {
    Text(
        text = model.title,
        modifier = modifier,
    )
}

private fun menuViewStateComposeStub() = MenuViewState(
    title = "Mutant Idle",
    buttons = persistentListOf(
        MenuButtonModel(id = 0L, title = "Start Game"),
        MenuButtonModel(id = 1L, title = "Settings"),
        MenuButtonModel(id = 2L, title = "Quit"),
    )
)
