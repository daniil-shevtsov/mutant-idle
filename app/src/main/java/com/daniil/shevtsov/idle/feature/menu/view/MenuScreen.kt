package com.daniil.shevtsov.idle.feature.menu.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Center
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import com.daniil.shevtsov.idle.core.ui.widgets.CavityButton
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuButtonModel
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuId
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.menuButtonModel
import kotlinx.collections.immutable.persistentListOf

@Preview(heightDp = 500)
@Composable
fun MenuScreenPreview() {
    MenuScreen(
        onClick = {},
        state = menuViewStateComposeStub()
    )
}

@Composable
fun MenuScreen(
    state: MenuViewState,
    onClick: (menuId: MenuId) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Center,
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background)
            .padding(AppTheme.dimensions.paddingL),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(AppTheme.dimensions.paddingL),
        ) {
            Text(
                text = state.title,
                style = AppTheme.typography.header,
                color = AppTheme.colors.textLight,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = spacedBy(AppTheme.dimensions.paddingM),
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                state.buttons.forEach { buttonModel ->
                    MenuButton(
                        model = buttonModel,
                        onClick = { onClick(buttonModel.id) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuButton(
    model: MenuButtonModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CavityButton(
        text = model.title,
        onClick = onClick,
        modifier = modifier,
    )
}

private fun menuViewStateComposeStub() = MenuViewState(
    title = "Mutant Idle",
    buttons = persistentListOf(
        menuButtonModel(title = "Start Game"),
        menuButtonModel(title = "Settings"),
    )
)
