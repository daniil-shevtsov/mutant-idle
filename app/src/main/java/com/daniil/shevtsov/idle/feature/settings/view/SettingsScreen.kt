package com.daniil.shevtsov.idle.feature.settings.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.daniil.shevtsov.idle.core.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Preview
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(
        state = settingsViewStateComposeStub()
    )
}

fun settingsViewStateComposeStub(): SettingsViewState {
    return SettingsViewState(
        categories = persistentListOf(
            SettingsCategoryModel(title = "General", isSelected = true),
            SettingsCategoryModel(title = "Accessibility", isSelected = false),
            SettingsCategoryModel(title = "Misc", isSelected = false),
        ),
        settingsPanel = SettingsPanelModel(
            items = persistentListOf(
                SettingsItem.Switch(id = 0L, title = "lol", isSelected = true),
                SettingsItem.Switch(id = 1L, title = "kek", isSelected = false),
                SettingsItem.Switch(id = 2L, title = "cheburek", isSelected = true),
                SettingsItem.ColorSelector(
                    id = 3L,
                    title = "lol color",
                    currentColor = SettingsColor("#FF0000")
                ),
                SettingsItem.Switch(id = 4L, title = "lol2", isSelected = true),
                SettingsItem.Switch(id = 5L, title = "kek2", isSelected = false),
                SettingsItem.ColorSelector(
                    id = 6L,
                    title = "cheburek color",
                    currentColor = SettingsColor("#00FF00")
                ),
            )
        )
    )
}

data class SettingsViewState(
    val categories: ImmutableList<SettingsCategoryModel>,
    val settingsPanel: SettingsPanelModel,
)

@Composable
fun SettingsScreen(
    state: SettingsViewState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(AppTheme.colors.background),
    ) {
        Column {
            state.categories.forEach { category ->
                SettingsCategory(category)
            }
        }
        Column {
            SettingsPanel(state.settingsPanel)
        }
    }
}

@Composable
fun SettingsCategory(model: SettingsCategoryModel) {
    Text(text = model.title)
}

@Composable
fun SettingsPanel(model: SettingsPanelModel) {
    Column {
        model.items.forEach { item ->
            when (item) {
                is SettingsItem.ColorSelector -> ColorSelector(item)
                is SettingsItem.Switch -> SettingsSwitch(item)
            }
        }
    }
}

@Composable
fun ColorSelector(model: SettingsItem.ColorSelector) {
    Text(text = "Color Selector ${model.id}: ${model.currentColor.hex}")
}

@Composable
fun SettingsSwitch(model: SettingsItem.Switch) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(AppTheme.dimensions.paddingM)
    ) {
        Text(text = model.title)
        Box {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(20.dp)
                    .background(AppTheme.colors.backgroundDark)
                    .align(Alignment.Center)
            )
            Box(
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .background(AppTheme.colors.backgroundLight)
                    .align(
                        when (model.isSelected) {
                            true -> Alignment.CenterEnd
                            false -> Alignment.CenterStart
                        }
                    )
            )
        }
    }
}

data class SettingsCategoryModel(
    val title: String,
    val isSelected: Boolean,
)

data class SettingsPanelModel(
    val items: ImmutableList<SettingsItem>
)

sealed interface SettingsItem {
    val title: String

    data class Switch(
        val id: Long,
        override val title: String,
        val isSelected: Boolean,
    ) : SettingsItem

    data class ColorSelector(
        val id: Long,
        override val title: String,
        val currentColor: SettingsColor,
    ) : SettingsItem
}

data class SettingsColor(
    val hex: String,
)
