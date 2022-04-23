package com.daniil.shevtsov.idle.feature.main.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.Mortician
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.resource
import com.daniil.shevtsov.idle.util.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainFunctionalCoreTest {
    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        val initialState = mainFunctionalCoreState(
            balanceConfig = balanceConfig(
                resourceSpentForFullMutant = 10.0
            ),
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
            ),
            upgrades = listOf(
                upgrade(id = 0L, price = 4.0)
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 0.0)
            )
        )

        val newState = mainFunctionalCore(
            state = initialState,
            action = MainViewAction.UpgradeSelected(id = 0L),
        )

        assertThat(newState)
            .all {
                prop(MainFunctionalCoreState::upgrades)
                    .extracting(Upgrade::id, Upgrade::status)
                    .containsExactly(0L to UpgradeStatus.Bought)

                prop(MainFunctionalCoreState::resources)
                    .extracting(Resource::key, Resource::value)
                    .containsExactly(ResourceKey.Blood to 6.0)

                prop(MainFunctionalCoreState::ratios)
                    .extracting(Ratio::key, Ratio::value)
                    .containsExactly(RatioKey.Mutanity to 0.4)
            }
    }

    @Test
    fun `should add job tags to player when job selected`() = runBlockingTest {
        val nonJobTags = listOf(
            tag(name = "non-job tag 1"),
            tag(name = "non-job tag 2"),
        )

        val previousPlayerState = player(
            job = playerJob(
                title = "old job",
                tags = listOf(
                    tag(name = "old job tag 1"),
                    tag(name = "old job tag 2"),
                )
            ),
            tags = listOf(
                tag(name = "old job tag 1"), //TODO: This design choice to have duplicated data is kinda dumb
                tag(name = "old job tag 2"),
            ) + nonJobTags
        )

        val newJob = playerJob(
            title = "new job",
            tags = listOf(
                tag(name = "new job tag 1"),
                tag(name = "new job tag 2"),
            ),
        )

        val initialState = mainFunctionalCoreState(
            player = previousPlayerState,
            availableJobs = listOf(
                previousPlayerState.job,
                newJob
            ),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            action = MainViewAction.DebugJobSelected(job = newJob)
        )

        assertThat(newState)
            .prop(MainFunctionalCoreState::player)
            .all {
                prop(Player::job)
                    .isEqualTo(newJob)

                prop(Player::tags)
                    .all {
                        containsSubList(nonJobTags)
                        containsNone(previousPlayerState.job.tags)
                        containsSubList(newJob.tags)
                    }
            }

    }
}