package com.daniil.shevtsov.idle.feature.gamestart.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsNone
import assertk.assertions.containsSubList
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.gamestart.presentation.GameStartViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.unlocks.domain.unlockState
import org.junit.jupiter.api.Test

internal class GameStartFunctionalCoreTest {

    @Test
    fun `should not do anything when locked job clicked`() {
        val currentJob = playerJob(id = 1L, title = "current job", tags = listOf(tag(name = "lol")))
        val newJob = playerJob(id = 2L, title = "new job", tags = listOf(tag(name = "kek")))
        val initialPlayer = player(
            job = currentJob
        )

        val initialState = gameState(
            player = initialPlayer,
            availableJobs = listOf(
                initialPlayer.job,
                newJob,
            ),
            unlockState = unlockState(
                jobs = mapOf(newJob.id to false)
            )
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.JobSelected(id = newJob.id)
        )

        assertThat(newState)
            .prop(GameState::player)
            .isEqualTo(initialPlayer)
    }

    @Test
    fun `should not do anything when locked species clicked`() {
        val currentSpecies =
            playerSpecies(id = 1L, title = "current species", tags = listOf(tag(name = "lol")))
        val newSpecies =
            playerSpecies(id = 2L, title = "new species", tags = listOf(tag(name = "kek")))
        val initialPlayer = player(
            species = currentSpecies
        )

        val initialState = gameState(
            player = initialPlayer,
            availableSpecies = listOf(
                initialPlayer.species,
                newSpecies,
            ),
            unlockState = unlockState(
                species = mapOf(newSpecies.id to false)
            )
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.SpeciesSelected(id = newSpecies.id)
        )

        assertThat(newState)
            .prop(GameState::player)
            .isEqualTo(initialPlayer)
    }

    @Test
    fun `should choose species when unlocked species clicked`() {
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
            species = previousSpecies,
            generalTags = nonSpeciesTags,
        )

        val initialState = gameState(
            player = previousPlayerState,
            availableSpecies = listOf(
                previousPlayerState.species,
                newSpecies
            ),
            unlockState = unlockState(
                species = mapOf(
                    newSpecies.id to true
                )
            ),
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.SpeciesSelected(id = newSpecies.id)
        )

        assertThat(newState)
            .prop(GameState::player)
            .all {
                prop(Player::species)
                    .isEqualTo(newSpecies)

                prop(Player::tags)
                    .all {
                        containsSubList(nonSpeciesTags)
                        containsNone(previousPlayerState.species.tags)
                        containsSubList(newSpecies.tags)
                    }
            }
    }

    @Test
    fun `should choose job when unlocked job clicked`() {
        val nonJobTags = listOf(
            tag(name = "non-species tag 1"),
            tag(name = "non-species tag 2"),
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
            generalTags = nonJobTags,
        )

        val initialState = gameState(
            player = previousPlayerState,
            availableJobs = listOf(
                previousPlayerState.job,
                newJob
            ),
            unlockState = unlockState(
                jobs = mapOf(
                    newJob.id to true
                )
            ),
        )

        val newState = gameStartFunctionalCore(
            state = initialState,
            viewAction = GameStartViewAction.JobSelected(id = newJob.id)
        )

        assertThat(newState)
            .prop(GameState::player)
            .all {
                prop(Player::job)
                    .isEqualTo(newJob)

                prop(Player::tags)
                    .all {
                        containsSubList(nonJobTags)
                        containsNone(previousPlayerState.species.tags)
                        containsSubList(newJob.tags)
                    }
            }
    }

    @Test
    fun `should open main screen when start game clicked`() {
        val newState = gameStartFunctionalCore(
            state = gameState(),
            viewAction = GameStartViewAction.StartGame,
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.Main)
    }

}
