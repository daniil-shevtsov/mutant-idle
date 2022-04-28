package com.daniil.shevtsov.idle.feature.main.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTab
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.main.presentation.sectionState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChange
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.resource
import kotlinx.coroutines.test.runBlockingTest
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
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 0.0)
            ),
            upgrades = listOf(
                upgrade(id = 0L, price = 4.0)
            ),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.UpgradeSelected(id = 0L),
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

        val initialState = mainFunctionalCoreState(
            availableJobs = listOf(
                previousPlayerState.job,
                newJob
            ),
            player = previousPlayerState,
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.DebugJobSelected(id = newJob.id)
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
            species = previousSpecies,
            generalTags = nonSpeciesTags,
        )

        val initialState = mainFunctionalCoreState(
            player = previousPlayerState,
            availableSpecies = listOf(
                previousPlayerState.species,
                newSpecies
            ),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.DebugSpeciesSelected(id = newSpecies.id)
        )

        assertThat(newState)
            .prop(MainFunctionalCoreState::player)
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
    fun `should update everything correctly when action clicked`() = runBlockingTest {
        val action = action(
            id = 1L,
            resourceChanges = mapOf(
                resourceChange(key = ResourceKey.Blood, change = 2.0),
                resourceChange(key = ResourceKey.Money, change = -7.0),
            ),
            ratioChanges = mapOf(
                ratioChange(key = RatioKey.Mutanity, change = 2.0f),
                ratioChange(key = RatioKey.Suspicion, change = -3.0f),
            )
        )

        val initialState = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 4.0),
                resource(key = ResourceKey.Money, value = 8.0),
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 3.0),
                ratio(key = RatioKey.Suspicion, value = 7.0),
            ),
            actions = listOf(action),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.ActionClicked(id = action.id),
        )

        assertThat(newState)
            .all {
                prop(MainFunctionalCoreState::resources)
                    .extracting(Resource::key, Resource::value)
                    .containsExactly(
                        ResourceKey.Blood to 6.0,
                        ResourceKey.Money to 1.0,
                    )
                prop(MainFunctionalCoreState::ratios)
                    .extracting(Ratio::key, Ratio::value)
                    .containsExactly(
                        RatioKey.Mutanity to 5.0,
                        RatioKey.Suspicion to 4.0,
                    )
            }
    }

    @Test
    fun `should select new tab when tab switched`() {
        val initialState = mainFunctionalCoreState(
            drawerTabs = listOf(
                drawerTab(id = DrawerTabId.PlayerInfo, isSelected = true),
                drawerTab(id = DrawerTabId.Debug, isSelected = false),
            ),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.DrawerTabSwitched(id = DrawerTabId.Debug)
        )

        assertThat(newState)
            .prop(MainFunctionalCoreState::drawerTabs)
            .extracting(DrawerTab::id, DrawerTab::isSelected)
            .containsExactly(
                DrawerTabId.PlayerInfo to false,
                DrawerTabId.Debug to true,
            )
    }

    @Test
    fun `should switch section collapsed state when clicked`() {
        val initialState = mainFunctionalCoreState(
            sections = listOf(
                sectionState(key = SectionKey.Resources, isCollapsed = false),
                sectionState(key = SectionKey.Actions, isCollapsed = false),
            ),
        )

        val firstState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.ToggleSectionCollapse(key = SectionKey.Resources)
        )

        assertThat(firstState)
            .prop(MainFunctionalCoreState::sections)
            .extracting(SectionState::key, SectionState::isCollapsed)
            .containsExactly(
                SectionKey.Resources to true,
                SectionKey.Actions to false,
            )

        val secondState = mainFunctionalCore(
            state = firstState,
            viewAction = MainViewAction.ToggleSectionCollapse(key = SectionKey.Resources)
        )

        assertThat(secondState)
            .prop(MainFunctionalCoreState::sections)
            .extracting(SectionState::key, SectionState::isCollapsed)
            .containsExactly(
                SectionKey.Resources to false,
                SectionKey.Actions to false,
            )
    }
}
