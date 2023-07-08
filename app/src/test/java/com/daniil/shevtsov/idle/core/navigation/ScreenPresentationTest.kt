package com.daniil.shevtsov.idle.core.navigation

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.gamefinish.domain.ending
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import org.junit.jupiter.api.Test

internal class ScreenPresentationTest {
    @Test
    fun `should form main view state when main screen selected`() {
        val state = gameState(currentScreen = Screen.Main)

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
        )

        val viewState = screenPresentationFunctionalCore(state = state)

        assertThat(viewState)
            .prop(ScreenHostViewState::contentState)
            .isInstanceOf(ScreenContentViewState.FinishedGame::class)
    }

    @Test
    fun `should use colors of devourer when it is selected`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(player = player(traits = mapOf(TraitId.Species to Species.Devourer)))
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .isEqualTo(Species.Devourer.colors)
    }

    @Test
    fun `should use colors of vampire when it is selected`() {
        val viewState = screenPresentationFunctionalCore(
            gameState(player = player(traits = mapOf(TraitId.Species to Species.Vampire)))
        )

        assertThat(viewState)
            .prop(ScreenHostViewState::colors)
            .isEqualTo(Species.Vampire.colors)
    }
}
