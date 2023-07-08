package com.daniil.shevtsov.idle.feature.settings.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
                SettingsItem.Switch(id = 0L, isSelected = true),
                SettingsItem.Switch(id = 1L, isSelected = false),
                SettingsItem.Switch(id = 2L, isSelected = true),
                SettingsItem.ColorSelector(id = 3L, currentColor = SettingsColor("#FF0000")),
                SettingsItem.Switch(id = 4L, isSelected = true),
                SettingsItem.Switch(id = 5L, isSelected = false),
                SettingsItem.ColorSelector(id = 6L, currentColor = SettingsColor("#00FF00")),
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
    Row(modifier = modifier) {
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
    Text(text = "Switch ${model.id}: ${model.isSelected}")
}

data class SettingsCategoryModel(
    val title: String,
    val isSelected: Boolean,
)

data class SettingsPanelModel(
    val items: ImmutableList<SettingsItem>
)

sealed interface SettingsItem {
    data class Switch(val id: Long, val isSelected: Boolean) : SettingsItem
    data class ColorSelector(val id: Long, val currentColor: SettingsColor) : SettingsItem
}

data class SettingsColor(
    val hex: String,
)
