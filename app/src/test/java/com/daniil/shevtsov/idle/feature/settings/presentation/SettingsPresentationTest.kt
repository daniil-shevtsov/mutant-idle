package com.daniil.shevtsov.idle.feature.settings.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.index
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsKey
import com.daniil.shevtsov.idle.feature.settings.domain.settings
import com.daniil.shevtsov.idle.feature.settings.domain.settingsCategory
import com.daniil.shevtsov.idle.feature.settings.domain.settingsControlBoolean
import com.daniil.shevtsov.idle.feature.settings.domain.settingsControlString
import com.daniil.shevtsov.idle.feature.settings.domain.settingsItem
import com.daniil.shevtsov.idle.feature.settings.view.SettingsCategoryModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsColor
import com.daniil.shevtsov.idle.feature.settings.view.SettingsItemModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsPanelModel
import com.daniil.shevtsov.idle.feature.settings.view.SettingsViewState
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class SettingsPresentationTest {

    @Test
    fun `should create view state`() {
        val viewState = mapSettingsViewState(
            state = gameState(
                settings = settings(
                    categories = persistentListOf(
                        settingsCategory(
                            id = 1L,
                            title = "General",
                        ),
                        settingsCategory(
                            id = 2L,
                            title = "Misc",
                        )
                    ),
                    selectedCategoryId = 1L,
                    settingsItems = persistentListOf(
                        settingsItem(
                            key = SettingsKey.DebugEnabled,
                            title = "Enable Debug",
                            value = settingsControlBoolean(isEnabled = false)
                        ),
                        settingsItem(
                            key = SettingsKey.BackgroundColor,
                            title = "Background Color",
                            value = settingsControlString(text = "#FF0000")
                        )
                    )
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            )
        )

        assertThat(viewState)
            .all {
                prop(SettingsViewState::categories)
                    .extracting(SettingsCategoryModel::id, SettingsCategoryModel::isSelected)
                    .containsExactly(1L to true, 2L to false)
                prop(SettingsViewState::settingsPanel)
                    .prop(SettingsPanelModel::items)
                    .all {
                        index(0)
                            .isInstanceOf(SettingsItemModel.Switch::class)
                            .all {
                                prop(SettingsItemModel.Switch::key).isEqualTo(SettingsKey.DebugEnabled)
                                prop(SettingsItemModel.Switch::title).isEqualTo("Enable Debug")
                                prop(SettingsItemModel.Switch::isSelected).isEqualTo(false)
                            }
                        index(1)
                            .isInstanceOf(SettingsItemModel.ColorSelector::class)
                            .all {
                                prop(SettingsItemModel.ColorSelector::key).isEqualTo(SettingsKey.BackgroundColor)
                                prop(SettingsItemModel.ColorSelector::title).isEqualTo("Background Color")
                                prop(SettingsItemModel.ColorSelector::currentColor).prop(SettingsColor::hex).isEqualTo("#FF0000")
                            }
                    }
            }
    }

}
