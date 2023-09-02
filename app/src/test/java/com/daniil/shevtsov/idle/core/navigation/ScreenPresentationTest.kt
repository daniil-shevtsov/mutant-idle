package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.colors.presentation.DomainColor
import com.daniil.shevtsov.idle.feature.colors.presentation.SpeciesColors
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.ending
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsControl
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsKey
import com.daniil.shevtsov.idle.feature.settings.domain.settings
import com.daniil.shevtsov.idle.feature.settings.domain.settingsItem
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test

internal class ScreenPresentationTest {
    @Test
    fun `should form main view state when main screen selected`() {
        val state = gameState(
            currentScreen = Screen.Main,
            gameTitle = MenuTitleState.Result("Mutant Idle")
        )

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState)
            .prop(ScreenHostViewState::contentState)
            .isInstanceOf(ScreenContentViewState.Main::class)
    }

    @Test
    fun `should form finished game view state when finished game screen selected`() {
        val state = gameState(
            currentScreen = Screen.FinishedGame,
            allEndings = listOf(ending(id = 1L)),
            currentEndingId = 1L,
            gameTitle = MenuTitleState.Result("Mutant Idle"),
        )

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState)
            .prop(ScreenHostViewState::contentState)
            .isInstanceOf(ScreenContentViewState.FinishedGame::class)
    }

    @Test
    fun `should use colors of devourer when it is selected`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(
                player = player(traits = mapOf(TraitId.Species to Species.Devourer)),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            )
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .isEqualTo(Species.Devourer.colors)
    }

    @Test
    fun `should use colors of vampire when it is selected`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(
                player = player(traits = mapOf(TraitId.Species to Species.Vampire)),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            )
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .isEqualTo(Species.Vampire.colors)
    }

    @Test
    fun `should use species colors when override colors settings disabled`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(
                player = player(traits = mapOf(TraitId.Species to Species.Devourer)),
                settings = settings(
                    settingsItems = persistentListOf(
                        settingsItem(
                            key = SettingsKey.ColorOverrideEnabled,
                            value = SettingsControl.BooleanValue(false)
                        ),
                        settingsItem(
                            key = SettingsKey.BackgroundColor,
                            value = SettingsControl.StringValue("#FF0000")
                        )
                    )
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            )
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .prop(SpeciesColors::background)
            .isEqualTo(Species.Devourer.colors?.background)
    }

    @Test
    fun `should use settings colors when override colors settings enabled`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(
                player = player(traits = mapOf(TraitId.Species to Species.Devourer)),
                settings = settings(
                    settingsItems = persistentListOf(
                        settingsItem(
                            key = SettingsKey.ColorOverrideEnabled,
                            value = SettingsControl.BooleanValue(true)
                        ),
                        settingsItem(
                            key = SettingsKey.BackgroundColor,
                            value = SettingsControl.StringValue("#FFFFFF")
                        )
                    )
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            )
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .prop(SpeciesColors::background)
            .isEqualTo(DomainColor(0xFFFFFFFF))
    }
}
