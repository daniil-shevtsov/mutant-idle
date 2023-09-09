package com.daniil.shevtsov.idle.feature.main.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.TurnInfo
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.turnInfo
import com.daniil.shevtsov.idle.feature.initial.domain.createInitialGameState
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChanges
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class TurnTest {

    @Test
    fun `should have 1 turn initially`() = runBlockingTest {
        val initialState = createInitialGameState()

        assertThat(initialState)
            .prop(GameState::currentTurn)
            .prop(TurnInfo::count)
            .isEqualTo(1)
    }

    @Test
    fun `should finish turn when selected action`() = runBlockingTest {
        val newState = mainFunctionalCore(
            state = gameState(
                currentTurn = turnInfo(count = 1),
                actions = listOf(action(id = 0L))
            ),
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .prop(GameState::currentTurn)
            .prop(TurnInfo::count)
            .isEqualTo(2)
    }

    @Test
    fun `should not finish turn when selected unavailable action`() = runBlockingTest {
        val newState = mainFunctionalCore(
            state = gameState(
                currentTurn = turnInfo(count = 1),
                actions = listOf(
                    action(
                        id = 0L,
                        resourceChanges = resourceChanges(ResourceKey.Blood to -10.0)
                    )
                ),
                resources = listOf(resource(key = ResourceKey.Blood, value = 0.0))
            ),
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .prop(GameState::currentTurn)
            .prop(TurnInfo::count)
            .isEqualTo(1)
    }

    @Test
    fun `should not finish turn when selected upgrade`() = runBlockingTest {
        val newState = mainFunctionalCore(
            state = gameState(
                currentTurn = turnInfo(count = 1),
                upgrades = listOf(upgrade(id = 0L))
            ),
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .prop(GameState::currentTurn)
            .prop(TurnInfo::count)
            .isEqualTo(1)
    }

    @Test
    fun `should not finish turn when selected location`() = runBlockingTest {
        val newState = mainFunctionalCore(
            state = gameState(
                currentTurn = turnInfo(count = 1),
                locations = listOf(location(id = 0L))
            ),
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .prop(GameState::currentTurn)
            .prop(TurnInfo::count)
            .isEqualTo(1)
    }
}
