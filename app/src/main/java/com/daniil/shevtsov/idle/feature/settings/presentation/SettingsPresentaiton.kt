package com.daniil.shevtsov.idle.feature.settings.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsControl
import com.daniil.shevtsov.idle.feature.settings.view.SettingsCategoryModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsColor
import com.daniil.shevtsov.idle.feature.settings.view.SettingsItemModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsPanelModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsViewState
import kotlinx.collections.immutable.toImmutableList

fun mapSettingsViewState(state: GameState): SettingsViewState {
    return SettingsViewState(
        categories = state.settings.categories.map { category ->
            SettingsCategoryModel(
                id = category.id,
                title = category.title,
                isSelected = category.id == state.settings.selectedCategoryId
            )
        }.toImmutableList(),
        settingsPanel = SettingsPanelModel(
            items = state.settings.settingsItems.map { settingsItem ->
                when (val control = settingsItem.value) {
                    is SettingsControl.BooleanValue -> SettingsItemModel.Switch(
                        id = 0L,
                        title = settingsItem.title,
                        isSelected = control.isEnabled
                    )

                    is SettingsControl.StringValue -> SettingsItemModel.ColorSelector(
                        id = 0L,
                        title = settingsItem.title,
                        currentColor = SettingsColor(control.text)
                    )
                }
            }.toImmutableList()
        )
    )
}
