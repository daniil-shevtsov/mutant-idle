package com.daniil.shevtsov.idle.feature.menu.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = spacedBy(AppTheme.dimensions.paddingL),
        modifier = modifier
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingL),
    ) {
        Text(
            text = state.title,
            style = AppTheme.typography.header,
            color = AppTheme.colors.textLight,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(AppTheme.dimensions.paddingM),
        ) {
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
    CavityButton(
        text = model.title,
        onClick = {},
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
