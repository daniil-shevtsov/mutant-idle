package com.daniil.shevtsov.idle.feature.settings.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCore
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

class SettingsFunctionalCoreTest {

    @Test
    fun `should create settings categories initially`() {
        val state = createInitialGameState()

        assertThat(state)
            .prop(GameState::settings)
            .all {
                prop(Settings::categories)
                    .extracting(SettingsCategory::id, SettingsCategory::title)
                    .containsExactly(
                        0L to "General",
                        1L to "Accessibility",
                    )
                prop(Settings::selectedCategoryId).isEqualTo(0L)
                prop(Settings::settingsItems)
                    .extracting(SettingsItem::key, SettingsItem::title)
                    .containsExactly(
                        SettingsKey.DebugEnabled to "Debug",
                        SettingsKey.ColorOverrideEnabled to "Custom Colors",
                        SettingsKey.BackgroundColor to "Background Color",
                    )
            }
    }

    @Test
    fun `should update settings item when settings switch switched`() {
        val state = mainFunctionalCore(
            state = gameState(
                settings = settings(
                    settingsItems = persistentListOf(
                        settingsItem(
                            key = SettingsKey.DebugEnabled,
                            value = settingsControlBoolean(isEnabled = false)
                        ),
                        settingsItem(
                            key = SettingsKey.BackgroundColor,
                            value = settingsControlString(text = "#FF0000")
                        ),
                        settingsItem(
                            key = SettingsKey.ColorOverrideEnabled,
                            value = settingsControlBoolean(isEnabled = false)
                        ),
                    )
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SettingsSwitchUpdate(key = SettingsKey.DebugEnabled)
        )

        assertThat(state)
            .prop(GameState::settings)
            .prop(Settings::settingsItems)
            .extracting(SettingsItem::key, SettingsItem::value)
            .containsExactly(
                SettingsKey.DebugEnabled to SettingsControl.BooleanValue(isEnabled = true),
                SettingsKey.BackgroundColor to SettingsControl.StringValue(text = "#FF0000"),
                SettingsKey.ColorOverrideEnabled to SettingsControl.BooleanValue(isEnabled = false),
            )
    }

    @Test
    fun `should update settings item when settings text saved`() {
        val state = mainFunctionalCore(
            state = gameState(
                settings = settings(
                    settingsItems = persistentListOf(
                        settingsItem(
                            key = SettingsKey.DebugEnabled,
                            value = settingsControlBoolean(isEnabled = false)
                        ),
                        settingsItem(
                            key = SettingsKey.BackgroundColor,
                            value = settingsControlString(text = "#FF0000")
                        ),
                        settingsItem(
                            key = SettingsKey.ColorOverrideEnabled,
                            value = settingsControlBoolean(isEnabled = false)
                        ),
                    )
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SettingsTextSaved(
                key = SettingsKey.BackgroundColor,
                newText = "#00FF00",
            )
        )

        assertThat(state)
            .prop(GameState::settings)
            .prop(Settings::settingsItems)
            .extracting(SettingsItem::key, SettingsItem::value)
            .containsExactly(
                SettingsKey.DebugEnabled to SettingsControl.BooleanValue(isEnabled = false),
                SettingsKey.BackgroundColor to SettingsControl.StringValue(text = "#00FF00"),
                SettingsKey.ColorOverrideEnabled to SettingsControl.BooleanValue(isEnabled = false),
            )
    }

}
