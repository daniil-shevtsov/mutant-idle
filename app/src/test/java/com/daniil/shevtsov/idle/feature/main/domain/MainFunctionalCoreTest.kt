package com.daniil.shevtsov.idle.feature.main.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.action.domain.ratioChanges
import com.daniil.shevtsov.idle.feature.action.domain.ratioChangesWithTags
import com.daniil.shevtsov.idle.feature.coreshell.domain.GameState
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.location.domain.LocationSelectionState
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.main.presentation.MainViewAction
import com.daniil.shevtsov.idle.feature.main.presentation.SectionKey
import com.daniil.shevtsov.idle.feature.main.presentation.SectionState
import com.daniil.shevtsov.idle.feature.main.presentation.sectionState
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChange
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class MainFunctionalCoreTest {
    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        val initialState = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 0.0)
            ),
            upgrades = listOf(
                upgrade(
                    id = 0L,
                    price = 4.0,
                    resourceChanges = mapOf(ResourceKey.Blood to -4.0),
                    ratioChanges = ratioChanges(RatioKey.Mutanity to 0.4),
                )
            ),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .all {
                prop(GameState::upgrades)
                    .extracting(Upgrade::id, Upgrade::status)
                    .containsExactly(0L to UpgradeStatus.Bought)

                prop(GameState::resources)
                    .extracting(Resource::key, Resource::value)
                    .containsExactly(ResourceKey.Blood to 6.0)

                prop(GameState::ratios)
                    .extracting(Ratio::key, Ratio::value)
                    .containsExactly(RatioKey.Mutanity to 0.4)
            }
    }

    @Test
    fun `should update resources and ratios when action clicked`() =
        runBlockingTest {
            val providedTag = tag(name = "lol")
            val action = action(
                id = 1L,
                resourceChanges = mapOf(
                    resourceChange(key = ResourceKey.Blood, change = 2.0),
                    resourceChange(key = ResourceKey.Money, change = -7.0),
                ),
                ratioChanges = ratioChangesWithTags(
                    RatioKey.Mutanity to ratioChange(change = 2.0),
                    RatioKey.Suspicion to ratioChange(change = -3.0),
                ),
            )

            val initialState = gameState(
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
                viewAction = MainViewAction.SelectableClicked(id = action.id),
            )

            assertThat(newState)
                .all {
                    prop(GameState::resources)
                        .extracting(Resource::key, Resource::value)
                        .containsExactly(
                            ResourceKey.Blood to 6.0,
                            ResourceKey.Money to 1.0,
                        )
                    prop(GameState::ratios)
                        .extracting(Ratio::key, Ratio::value)
                        .containsExactly(
                            RatioKey.Mutanity to 5.0,
                            RatioKey.Suspicion to 4.0,
                        )
                }
        }

    @Test
    fun `should switch section collapsed state when clicked`() {
        val initialState = gameState(
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
            .prop(GameState::sections)
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
            .prop(GameState::sections)
            .extracting(SectionState::key, SectionState::isCollapsed)
            .containsExactly(
                SectionKey.Resources to false,
                SectionKey.Actions to false,
            )
    }

    @Test
    fun `should change location selection expanded state when switched`() {
        val stateAfterFirstSwitch = mainFunctionalCore(
            state = gameState(
                locationSelectionState = locationSelectionState(
                    isSelectionExpanded = false,
                )
            ),
            viewAction = MainViewAction.LocationSelectionExpandChange,
        )
        assertThat(stateAfterFirstSwitch)
            .prop(GameState::locationSelectionState)
            .prop(LocationSelectionState::isSelectionExpanded)
            .isEqualTo(true)

        val stateAfterSecondSwitch = mainFunctionalCore(
            state = stateAfterFirstSwitch,
            viewAction = MainViewAction.LocationSelectionExpandChange,
        )
        assertThat(stateAfterSecondSwitch)
            .prop(GameState::locationSelectionState)
            .prop(LocationSelectionState::isSelectionExpanded)
            .isEqualTo(false)
    }

    @Test
    fun `should select location when clicked`() {
        val location = location(id = 1L, title = "lol")

        val state = mainFunctionalCore(
            state = gameState(
                locations = listOf(location),
                locationSelectionState = locationSelectionState()
            ),
            viewAction = MainViewAction.SelectableClicked(id = location.id)
        )

        assertThat(state)
            .prop(GameState::locationSelectionState)
            .prop(LocationSelectionState::selectedLocation)
            .isEqualTo(location)
    }

    @Test
    fun `should open finished screen if suspicion gained maximum value`() {
        val state = gameState(
            ratios = listOf(
                ratio(key = RatioKey.Suspicion, value = 0.95),
            ),
            actions = listOf(
                action(id = 1L, ratioChanges = ratioChanges(RatioKey.Suspicion to 0.05))
            )
        )

        val newState = mainFunctionalCore(
            state = state,
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.FinishedGame)
    }

}
