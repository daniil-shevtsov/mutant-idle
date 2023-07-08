package com.daniil.shevtsov.idle.feature.settings.presentation

import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.settings.view.SettingsPanelModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsViewState
import kotlinx.collections.immutable.persistentListOf

fun mapSettingsViewState(state: GameState): SettingsViewState {
    return SettingsViewState(
        categories = persistentListOf(),
        settingsPanel = SettingsPanelModel(
            items = persistentListOf()
        )
    )
}
