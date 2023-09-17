package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.feature.colors.presentation.DomainColor
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerPresentation
import com.daniil.shevtsov.idle.feature.gamefinish.presentation.mapFinishedGameViewState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.mapCharacterSelectionViewState
import com.daniil.shevtsov.idle.feature.main.presentation.mapMainViewState
import com.daniil.shevtsov.idle.feature.menu.presentation.mapMainMenuViewState
import com.daniil.shevtsov.idle.feature.player.species.domain.Species.Devourer
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsControl
import com.daniil.shevtsov.idle.feature.settings.domain.SettingsKey
import com.daniil.shevtsov.idle.feature.settings.presentation.mapSettingsViewState

fun screenPresentationFunctionalCore(
    state: GameState
): ScreenHostViewState {
    val drawerState = drawerPresentation(state)
    val contentState = when (state.currentScreen) {
        Screen.Menu -> ScreenContentViewState.MainMenu(mapMainMenuViewState(state))
        Screen.Settings -> ScreenContentViewState.Settings(mapSettingsViewState(state))
        Screen.GameStart -> ScreenContentViewState.GameStart(mapCharacterSelectionViewState(state))
        Screen.Main -> ScreenContentViewState.Main(mapMainViewState(state))
        Screen.FinishedGame -> ScreenContentViewState.FinishedGame(mapFinishedGameViewState(state))
    }

    val speciesColors = (state.player.traits[TraitId.Species]?.colors ?: Devourer.colors)!!
    val isColorOverrideEnabled =
        (state.settings.settingsItems.find { it.key == SettingsKey.ColorOverrideEnabled }?.value as? SettingsControl.BooleanValue)?.isEnabled
            ?: false
    val backgroundColorSetting =
        (state.settings.settingsItems.find { it.key == SettingsKey.BackgroundColor }?.value as? SettingsControl.StringValue)?.text
    val parsedArgb: Long? =
        runCatching {
            ("ff" + backgroundColorSetting.orEmpty().removePrefix("#").lowercase()).toLong(16)
        }.getOrNull()

    val colorsToUse = when {
        isColorOverrideEnabled && parsedArgb != null -> speciesColors.copy(
            background = DomainColor(parsedArgb)
        )

        else -> speciesColors
    }

    return ScreenHostViewState(
        speciesId = state.player.traits[TraitId.Species]?.id ?: Devourer.id,
        colors = colorsToUse,
        drawerState = drawerState,
        contentState = contentState,
    )
}
