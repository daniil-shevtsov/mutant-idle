package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import org.junit.jupiter.api.Test

internal class ScreenFunctionalCoreTest {
    @Test
    fun `should open finished screen if suspicion gained maximum value`() {
        val state = gameState(
            ratios = listOf(
                ratio(key = RatioKey.Suspicion, value = 0.95),
            ),
            actions = listOf(
                action(id = 1L, ratioChanges = mapOf(RatioKey.Suspicion to 0.05f))
            )
        )

        val newState = screenFunctionalCore(
            state = state,
            viewAction = ScreenViewAction.Main(MainViewAction.ActionClicked(id = 1L))
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.FinishedGame)
    }
}
