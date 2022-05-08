package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewAction
import com.daniil.shevtsov.idle.feature.drawer.domain.drawerFunctionalCore
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerViewAction
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.assertJobSelected
import com.daniil.shevtsov.idle.feature.player.core.domain.assertSpeciesSelected
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.player.trait.domain.toPlayerTrait
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class DrawerFunctionalCoreTest {

    @Test
    fun `should change player trait when trait selected`() = runBlockingTest {
        val newSpecies = playerTrait(traitId = TraitId.Species)
        val initialState = gameState(availableTraits = listOf(newSpecies))

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.Debug(
                DebugViewAction.TraitSelected(
                    traitId = newSpecies.traitId,
                    id = newSpecies.id
                )
            )
        )

        assertThat(newState)
            .prop(GameState::player)
            .assertSpeciesSelected(id = newSpecies.id)
    }

    @Test
    fun `should select new tab when tab switched`() {
        val initialState = gameState(
            drawerTabs = listOf(
                drawerTab(id = DrawerTabId.PlayerInfo, isSelected = true),
                drawerTab(id = DrawerTabId.Debug, isSelected = false),
            ),
        )

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.TabSwitched(id = DrawerTabId.Debug)
        )

        assertThat(newState)
            .prop(GameState::drawerTabs)
            .extracting(DrawerTab::id, DrawerTab::isSelected)
            .containsExactly(
                DrawerTabId.PlayerInfo to false,
                DrawerTabId.Debug to true,
            )
    }

    @Test
    fun `should unlock jobs and species if unlock everything clicked`() {
        val lockedJob = playerJob(id = 1L, title = "locked job")
        val unlockedJob = playerJob(id = 2L, title = "unlocked job")

        val lockedSpecies = playerSpecies(id = 3L, title = "locked species")
        val unlockedSpecies = playerSpecies(id = 4L, title = "unlocked species")

        val initialState = gameState(
            availableTraits = listOf(
                lockedJob.toPlayerTrait(),
                unlockedJob.toPlayerTrait(),
                lockedSpecies.toPlayerTrait(),
                unlockedSpecies.toPlayerTrait(),
            ),
            unlockState = unlockState(
                jobs = mapOf(lockedJob.id to false, unlockedJob.id to true),
                species = mapOf(lockedSpecies.id to false, unlockedSpecies.id to true),
                traits = mapOf(
                    TraitId.Species to mapOf(lockedJob.id to false, unlockedJob.id to true),
                    TraitId.Job to mapOf(lockedSpecies.id to false, unlockedSpecies.id to true),
                )
            )
        )

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.Debug(DebugViewAction.UnlockEverything),
        )

        assertThat(newState)
            .prop(GameState::unlockState)
            .all {
                prop(UnlockState::traits).all {
                    contains(
                        TraitId.Job, mapOf(
                            lockedJob.id to true,
                            unlockedJob.id to true,
                        )
                    )
                    contains(
                        TraitId.Species, mapOf(
                            lockedSpecies.id to true,
                            unlockedSpecies.id to true,
                        )
                    )
                }
            }
    }

}
