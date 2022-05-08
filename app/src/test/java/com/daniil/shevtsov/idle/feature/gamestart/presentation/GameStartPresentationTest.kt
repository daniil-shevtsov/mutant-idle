package com.daniil.shevtsov.idle.feature.gamestart.presentation

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import org.junit.jupiter.api.Test

internal class GameStartPresentationTest {

    @Test
    fun `should show title initially`() {
        val state = gameState()

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .isNotNull()
    }

    @Test
    fun `should show available species selection initially`() {
        val species1 = playerSpecies(id = 1L, title = "species 1", description = "description 1")
        val species2 = playerSpecies(id = 2L, title = "species 2", description = "description 2")
        val state = gameState(
            availableSpecies = listOf(
                species1,
                species2,
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .all {
                index(0).all {
                    prop(TraitSelectionItem::title).isEqualTo(species1.title)
                    prop(TraitSelectionItem::description).isEqualTo(species1.description)
                    prop(TraitSelectionItem::icon).isNotNull().isEqualTo(species1.icon)
                }
                index(1).all {
                    prop(TraitSelectionItem::title).isEqualTo(species2.title)
                    prop(TraitSelectionItem::description).isEqualTo(species2.description)
                    prop(TraitSelectionItem::icon).isNotNull().isEqualTo(species2.icon)
                }
            }
    }

    @Test
    fun `should mark locked species as locked`() {
        val unlockedSpecies = playerSpecies(id = 1L, title = "unlocked species")
        val lockedSpecies = playerSpecies(id = 2L, title = "locked species")

        val state = gameState(
            availableSpecies = listOf(
                unlockedSpecies,
                lockedSpecies,
            ),
            unlockState = unlockState(
                species = mapOf(
                    unlockedSpecies.id to true,
                    lockedSpecies.id to false,
                )
            )
        )
        val viewState = mapGameStartViewState(state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .extracting(TraitSelectionItem::title, TraitSelectionItem::isUnlocked)
            .containsExactly(
                unlockedSpecies.title to true,
                lockedSpecies.title to false,
            )
    }

    @Test
    fun `should show selected species as selected`() {
        val notSelectedSpecies =
            playerTrait(id = 1L, traitId = TraitId.Species, title = "not selected species")
        val selectedSpecies =
            playerTrait(id = 2L, traitId = TraitId.Species, title = "selected species")
        val state = gameState(
            availableTraits = listOf(
                notSelectedSpecies,
                selectedSpecies,
            ),
            player = player(
                traits = mapOf(
                    TraitId.Species to selectedSpecies
                ),
            ),
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::speciesSelection)
            .extracting(TraitSelectionItem::title, TraitSelectionItem::isSelected)
            .containsExactly(
                notSelectedSpecies.title to false,
                selectedSpecies.title to true,
            )
    }

    @Test
    fun `should show available job selection initially`() {
        val job1 = playerJob(id = 1L, title = "job 1", description = "description 1")
        val job2 = playerJob(id = 2L, title = "job 2", description = "description 2")
        val state = gameState(
            availableJobs = listOf(
                job1,
                job2,
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::jobSelection)
            .all {
                index(0).all {
                    prop(TraitSelectionItem::title).isEqualTo(job1.title)
                    prop(TraitSelectionItem::description).isEqualTo(job1.description)
                }
                index(1).all {
                    prop(TraitSelectionItem::title).isEqualTo(job2.title)
                    prop(TraitSelectionItem::description).isEqualTo(job2.description)
                }
            }
    }

    @Test
    fun `should mark locked jobs as locked`() {
        val unlockedJob = playerJob(id = 1L, title = "unlocked job")
        val lockedJob = playerJob(id = 2L, title = "locked job")

        val state = gameState(
            availableJobs = listOf(
                unlockedJob,
                lockedJob,
            ),
            unlockState = unlockState(
                jobs = mapOf(
                    unlockedJob.id to true,
                    lockedJob.id to false,
                )
            )
        )
        val viewState = mapGameStartViewState(state)

        assertThat(viewState)
            .prop(GameStartViewState::jobSelection)
            .extracting(TraitSelectionItem::title, TraitSelectionItem::isUnlocked)
            .containsExactly(
                unlockedJob.title to true,
                lockedJob.title to false,
            )
    }

    @Test
    fun `should show selected job as selected`() {
        val notSelectedJob = playerTrait(id = 1L, traitId = TraitId.Job, title = "not selected job")
        val selectedJob = playerTrait(id = 2L, traitId = TraitId.Job, title = "selected job")
        val state = gameState(
            availableTraits = listOf(
                notSelectedJob,
                selectedJob,
            ),
            player = player(
                traits = mapOf(
                    TraitId.Job to selectedJob
                )
            )
        )

        val viewState = mapGameStartViewState(state = state)

        assertThat(viewState)
            .prop(GameStartViewState::jobSelection)
            .extracting(TraitSelectionItem::title, TraitSelectionItem::isSelected)
            .containsExactly(
                notSelectedJob.title to false,
                selectedJob.title to true,
            )
    }

}
