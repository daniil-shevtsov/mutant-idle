package com.daniil.shevtsov.idle.feature.main.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsNone
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
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChange
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
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
            viewAction = MainViewAction.UpgradeSelected(id = 0L),
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
    fun `should update tags correctly when upgrade bought`() =
        runBlockingTest {
            val providedTag = tag(name = "lol")

            val initialState = gameState(
                resources = listOf(resource(key = ResourceKey.Blood, value = 1.0)),
                ratios = listOf(ratio(key = RatioKey.Mutanity, value = 0.2)),
                upgrades = listOf(
                    upgrade(
                        id = 1L,
                        tags = mapOf(TagRelation.Provides to listOf(providedTag)),
                        resourceChanges = mapOf(ResourceKey.Blood to -0.4),
                        ratioChanges = ratioChanges(RatioKey.Mutanity to 0.5),
                    )
                ),
            )

            val newState = mainFunctionalCore(
                state = initialState,
                viewAction = MainViewAction.UpgradeSelected(id = 1L),
            )

            assertThat(newState)
                .prop(GameState::player)
                .prop(Player::tags)
                .containsExactly(providedTag)
        }

    @Test
    fun `should update resources, ratios and tags correctly when action clicked`() =
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
                tags = mapOf(
                    TagRelation.Provides to listOf(providedTag)
                )
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
                viewAction = MainViewAction.ActionClicked(id = action.id),
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
                    prop(GameState::player)
                        .prop(Player::tags)
                        .containsExactly(providedTag)
                }
        }

    @Test
    fun `should add tags provided by clicked action`() {
        val providedTag = tag(name = "lol")
        val action = action(
            id = 1L,
            tags = mapOf(TagRelation.Provides to listOf(providedTag)),
        )
        val newState = mainFunctionalCore(
            state = gameState(actions = listOf(action)),
            viewAction = MainViewAction.ActionClicked(id = action.id),
        )

        assertThat(newState)
            .prop(GameState::player)
            .prop(Player::tags)
            .containsExactly(providedTag)
    }

    @Test
    fun `should remove tags removed by clicked action`() {
        val tagToRemove = tag(name = "lol")
        val action = action(
            id = 1L,
            tags = mapOf(TagRelation.Removes to listOf(tagToRemove)),
        )
        val newState = mainFunctionalCore(
            state = gameState(
                player = player(generalTags = listOf(tagToRemove)),
                actions = listOf(action),
            ),
            viewAction = MainViewAction.ActionClicked(id = action.id),
        )

        assertThat(newState)
            .prop(GameState::player)
            .prop(Player::tags)
            .containsNone(tagToRemove)
    }

    //TODO: I think I need some abstractions over action, upgrade and locations
    @Test
    fun `should add plot by clicked action`() {
        val action = action(
            id = 1L,
            plot = "bla bla bla",
        )
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(action),
            ),
            viewAction = MainViewAction.ActionClicked(id = action.id),
        )

        assertThat(newState)
            .prop(GameState::plotEntries)
            .extracting(PlotEntry::text)
            .containsExactly(action.plot)
    }

    @Test
    fun `should add default plot entry when action plot is empty `() {
        val action = action(
            id = 1L,
            title = "Lol",
            plot = null,
        )
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(action),
            ),
            viewAction = MainViewAction.ActionClicked(id = action.id),
        )

        assertThat(newState)
            .prop(GameState::plotEntries)
            .extracting(PlotEntry::text)
            .containsExactly("You performed action \"Lol\"")
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
                locationSelectionState = locationSelectionState(
                    allLocations = listOf(location)
                )
            ),
            viewAction = MainViewAction.LocationSelected(id = location.id)
        )

        assertThat(state)
            .prop(GameState::locationSelectionState)
            .prop(LocationSelectionState::selectedLocation)
            .isEqualTo(location)
    }

    @Test
    fun `should update current tags with provided by selected location`() {
        val oldLocation = location(
            id = 1L, title = "old location", tags = mapOf(
                TagRelation.Provides to listOf(tag(name = "old tag"))
            )
        )

        val newTag = tag(name = "new tag")
        val newLocation = location(
            id = 2L, title = "new location", tags = mapOf(
                TagRelation.Provides to listOf(newTag)
            )
        )

        val state = mainFunctionalCore(
            state = gameState(
                locationSelectionState = locationSelectionState(
                    allLocations = listOf(oldLocation, newLocation)
                )
            ),
            viewAction = MainViewAction.LocationSelected(id = newLocation.id)
        )

        assertThat(state)
            .prop(GameState::player)
            .prop(Player::generalTags)
            .containsExactly(newTag)
    }

    @Test
    fun `should add plot entry for selected location`() {
        val location = location(
            id = 1L,
            title = "old location",
            plot = "test lol test",
            tags = mapOf(
                TagRelation.Provides to listOf(tag(name = "old tag"))
            )
        )

        val state = mainFunctionalCore(
            state = gameState(
                locationSelectionState = locationSelectionState(
                    allLocations = listOf(location)
                )
            ),
            viewAction = MainViewAction.LocationSelected(id = location.id)
        )

        assertThat(state)
            .prop(GameState::plotEntries)
            .extracting(PlotEntry::text)
            .containsExactly(location.plot)
    }

    @Test
    fun `should add default plot entry when location plot is empty`() {
        val location = location(
            id = 1L,
            title = "Test Location",
            plot = null,
            tags = mapOf(
                TagRelation.Provides to listOf(tag(name = "old tag"))
            )
        )

        val state = mainFunctionalCore(
            state = gameState(
                locationSelectionState = locationSelectionState(
                    allLocations = listOf(location)
                )
            ),
            viewAction = MainViewAction.LocationSelected(id = location.id)
        )

        assertThat(state)
            .prop(GameState::plotEntries)
            .extracting(PlotEntry::text)
            .containsExactly("You went to the Test Location")
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
            viewAction = MainViewAction.ActionClicked(id = 1L)
        )

        assertThat(newState)
            .prop(GameState::currentScreen)
            .isEqualTo(Screen.FinishedGame)
    }

}
