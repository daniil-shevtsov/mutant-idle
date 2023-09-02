package com.daniil.shevtsov.idle.feature.menu.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.menuButtonModel
import kotlinx.collections.immutable.persistentListOf

@Preview(heightDp = 500)
@Composable
fun MenuScreenPreview() {
    listOf(
        MenuTitleViewState.Loading,
        MenuTitleViewState.Result("Mutant Idle"),
        MenuTitleViewState.Error,
    ).forEach { titleState ->
        Column {
            MenuScreen(
                onClick = {},
                state = menuViewStateComposeStub().copy(title = titleState)
            )
        }
    }
}

@Composable
fun GameTitle(
    newTitle: MenuTitleViewState,
    modifier: Modifier = Modifier,
) {
    val text = when (newTitle) {
        is MenuTitleViewState.Error -> "ERROR"
        is MenuTitleViewState.Loading -> "Getting the title..."
        is MenuTitleViewState.Result -> newTitle.text
    }

    Text(
        text = text,
        style = AppTheme.typography.header,
        color = AppTheme.colors.textLight,
        modifier = modifier,
    )

}

@Composable
fun MenuScreen(
    state: MenuViewState,
    onClick: (menuId: MenuId) -> Unit,
    onTitleClick: () -> Unit = {},
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
            GameTitle(state.title, modifier = Modifier.clickable {
                onTitleClick()
            })
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
    title = MenuTitleViewState.Result("Mutant Idle"),
    buttons = persistentListOf(
        menuButtonModel(title = "Start Game"),
        menuButtonModel(title = "Settings"),
    )
)
