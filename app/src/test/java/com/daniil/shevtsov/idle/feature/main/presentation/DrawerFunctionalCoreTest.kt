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
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.unlocks.domain.UnlockState
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

internal class DrawerFunctionalCoreTest {

    @Test
    fun `should change player job when job selected`() = runBlockingTest {
        val nonJobTags = listOf(
            tag(name = "non-job tag 1"),
            tag(name = "non-job tag 2"),
        )

        val previousJob = playerJob(
            id = 0L,
            title = "old job",
            tags = listOf(
                tag(name = "old job tag 1"),
                tag(name = "old job tag 2"),
            )
        )
        val newJob = playerJob(
            id = 1L,
            title = "new job",
            tags = listOf(
                tag(name = "new job tag 1"),
                tag(name = "new job tag 2"),
            ),
        )

        val previousPlayerState = player(
            job = previousJob,
            generalTags = nonJobTags
        )

        val initialState = gameState(
            availableJobs = listOf(
                previousJob,
                newJob
            ),
            player = previousPlayerState,
        )

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.Debug(DebugViewAction.JobSelected(id = newJob.id))
        )

        assertThat(newState)
            .prop(GameState::player)
            .all {
                assertJobSelected(id = newJob.id)

                prop(Player::tags)
                    .all {
                        containsSubList(nonJobTags)
                        containsNone(previousJob.tags)
                        containsSubList(newJob.tags)
                    }
            }
    }

    @Test
    fun `should change player species when species selected`() = runBlockingTest {
        val nonSpeciesTags = listOf(
            tag(name = "non-species tag 1"),
            tag(name = "non-species tag 2"),
        )

        val previousSpecies = playerSpecies(
            id = 0L,
            title = "old species",
            tags = listOf(
                tag(name = "old species tag 1"),
                tag(name = "old species tag 2"),
            )
        )
        val newSpecies = playerSpecies(
            id = 1L,
            title = "new species",
            tags = listOf(
                tag(name = "new species tag 1"),
                tag(name = "new species tag 2"),
            ),
        )

        val previousPlayerState = player(
            generalTags = nonSpeciesTags,
        )

        val initialState = gameState(
            player = previousPlayerState,
            availableSpecies = listOf(
                previousSpecies,
                newSpecies
            ),
        )

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.Debug(DebugViewAction.SpeciesSelected(id = newSpecies.id))
        )

        assertThat(newState)
            .prop(GameState::player)
            .all {
                assertSpeciesSelected(id = newSpecies.id)

                prop(Player::tags)
                    .all {
                        containsSubList(nonSpeciesTags)
                        containsNone(previousSpecies.tags)
                        containsSubList(newSpecies.tags)
                    }
            }
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
            availableJobs = listOf(lockedJob, unlockedJob),
            availableSpecies = listOf(lockedSpecies, unlockedSpecies),
            unlockState = unlockState(
                jobs = mapOf(lockedJob.id to false, unlockedJob.id to true),
                species = mapOf(lockedSpecies.id to false, unlockedSpecies.id to true),
            )
        )

        val newState = drawerFunctionalCore(
            state = initialState,
            viewAction = DrawerViewAction.Debug(DebugViewAction.UnlockEverything),
        )

        assertThat(newState)
            .prop(GameState::unlockState)
            .all {
                prop(UnlockState::jobs).containsOnly(
                    lockedJob.id to true,
                    unlockedJob.id to true,
                )
                prop(UnlockState::species).containsOnly(
                    lockedSpecies.id to true,
                    unlockedSpecies.id to true,
                )
            }
    }

}
