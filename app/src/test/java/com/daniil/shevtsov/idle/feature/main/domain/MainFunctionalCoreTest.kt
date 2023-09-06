package com.daniil.shevtsov.idle.feature.main.domain

import assertk.all
import assertk.assertThat
import assertk.assertions.containsAll
import assertk.assertions.containsExactly
import assertk.assertions.extracting
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.core.navigation.Screen
import com.daniil.shevtsov.idle.feature.action.domain.Actions
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
import com.daniil.shevtsov.idle.feature.menu.presentation.MenuTitleState
import com.daniil.shevtsov.idle.feature.player.core.domain.Player
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.ratioChange
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChange
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChanges
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrades
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class MainFunctionalCoreTest {
    @Test
    fun `should add detective when investigation suspicion level reached`() = runBlockingTest {
        val initialState = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
                resource(key = ResourceKey.Detective, value = 0.0),
            ),
            actions = listOf(
                action(
                    id = 0L,
                    title = "Litter",
                    ratioChanges = ratioChanges(RatioKey.Suspicion to 0.13)
                )
            ),
            ratios = listOf(
                ratio(key = RatioKey.Suspicion, value = 0.49),
            ),
            gameTitle = MenuTitleState.Result("Mutant Idle"),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState)
            .all {
                prop(GameState::resources)
                    .extracting(Resource::key, Resource::value)
                    .containsExactly(
                        ResourceKey.Blood to 10.0,
                        ResourceKey.Detective to 1.0,
                    )
                prop(GameState::ratios)
                    .extracting(Ratio::key, Ratio::value)
                    .containsExactly(RatioKey.Suspicion to 0.62)
            }
    }

    @Test
    fun `should add only one detective after investigation reached`() = runBlockingTest {
        val initialState = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
                resource(key = ResourceKey.Detective, value = 0.0),
            ),
            actions = listOf(
                action(
                    id = 0L,
                    title = "Litter",
                    ratioChanges = ratioChanges(RatioKey.Suspicion to 0.13)
                )
            ),
            ratios = listOf(
                ratio(key = RatioKey.Suspicion, value = 0.49),
            ),
            gameTitle = MenuTitleState.Result("Mutant Idle"),
        )

        val newState = mainFunctionalCore(
            state = initialState,
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )
        val newState2 = mainFunctionalCore(
            state = newState,
            viewAction = MainViewAction.SelectableClicked(id = 0L),
        )

        assertThat(newState2)
            .all {
                prop(GameState::resources)
                    .extracting(Resource::key, Resource::value)
                    .containsExactly(
                        ResourceKey.Blood to 10.0,
                        ResourceKey.Detective to 1.0,
                    )
            }
    }

    @Test
    fun `should add 1 % suspicion every action when has 1 detective investigating`() =
        runBlockingTest {
            val initialState = gameState(
                resources = listOf(
                    resource(key = ResourceKey.Blood, value = 10.0),
                    resource(key = ResourceKey.Detective, value = 1.0),
                ),
                actions = listOf(
                    action(
                        id = 0L,
                        title = "Wait a bit",
                    )
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Suspicion, value = 0.60),
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            )

            val newState = mainFunctionalCore(
                state = initialState,
                viewAction = MainViewAction.SelectableClicked(id = 0L),
            )

            assertThat(newState)
                .prop(GameState::ratios)
                .extracting(Ratio::key, Ratio::value)
                .containsExactly(RatioKey.Suspicion to 0.61)
        }

    @Test
    fun `should add 5 % suspicion every action when has 5 detective investigating`() =
        runBlockingTest {
            val initialState = gameState(
                resources = listOf(
                    resource(key = ResourceKey.Blood, value = 10.0),
                    resource(key = ResourceKey.Detective, value = 5.0),
                ),
                actions = listOf(
                    action(
                        id = 0L,
                        title = "Wait a bit",
                    )
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Suspicion, value = 0.60),
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            )

            val newState = mainFunctionalCore(
                state = initialState,
                viewAction = MainViewAction.SelectableClicked(id = 0L),
            )

            assertThat(newState)
                .prop(GameState::ratios)
                .extracting(Ratio::key, Ratio::value)
                .containsExactly(RatioKey.Suspicion to 0.65)
        }

    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        val initialState = gameState(
            upgrades = listOf(
                upgrade(
                    id = 0L,
                    price = 4.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -4.0),
                    ratioChanges = ratioChanges(RatioKey.Mutanity to 0.4),
                )
            ),
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 10.0),
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 0.0)
            ),
            gameTitle = MenuTitleState.Result("Mutant Idle"),
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
                resourceChanges = resourceChanges(
                    resourceChange(key = ResourceKey.Blood, change = 2.0),
                    resourceChange(key = ResourceKey.Money, change = -7.0),
                ),
                ratioChanges = ratioChangesWithTags(
                    RatioKey.Mutanity to ratioChange(change = 2.0),
                    RatioKey.Suspicion to ratioChange(change = -3.0),
                ),
            )

            val initialState = gameState(
                actions = listOf(action),
                resources = listOf(
                    resource(key = ResourceKey.Blood, value = 4.0),
                    resource(key = ResourceKey.Money, value = 8.0),
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, value = 3.0),
                    ratio(key = RatioKey.Suspicion, value = 7.0),
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            )

            val newState = mainFunctionalCore(
                state = initialState,
                viewAction = MainViewAction.SelectableClicked(id = action.id),
            )

            assertThat(newState)
                .all {
                    prop(GameState::resources)
                        .extracting(Resource::key, Resource::value)
                        .containsAll(
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
    fun `should change ratio to zero if it gets negative after action clicked`() =
        runBlockingTest {
            val providedTag = tag(name = "lol")
            val action = action(
                id = 1L,
                ratioChanges = ratioChangesWithTags(RatioKey.Suspicion to ratioChange(change = -3.0)),
            )

            val initialState = gameState(
                actions = listOf(action),
                ratios = listOf(ratio(key = RatioKey.Suspicion, value = 1.0)),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            )

            val newState = mainFunctionalCore(
                state = initialState,
                viewAction = MainViewAction.SelectableClicked(id = action.id),
            )

            assertThat(newState)
                .prop(GameState::ratios)
                .extracting(Ratio::key, Ratio::value)
                .containsExactly(RatioKey.Suspicion to 0.0)
        }

    @Test
    fun `should switch section collapsed state when clicked`() {
        val initialState = gameState(
            sections = listOf(
                sectionState(key = SectionKey.Resources, isCollapsed = false),
                sectionState(key = SectionKey.Actions, isCollapsed = false),
            ),
            gameTitle = MenuTitleState.Result("Mutant Idle"),
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
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
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
                locationSelectionState = locationSelectionState(),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SelectableClicked(id = location.id)
        )

        assertThat(state)
            .prop(GameState::locationSelectionState)
            .prop(LocationSelectionState::selectedLocation)
            .isEqualTo(location)
    }

    @Test
    fun `should remove provided by location tag when another location selected`() {
        val oldTag = tag(name = "old location")
        val newTag = tag(name = "new location")
        val otherTag = tag(name = "other tag")
        val oldLocation = location(
            id = 1L,
            title = "old location",
            tagRelations = tagRelations(TagRelation.Provides to oldTag)
        )
        val newLocation = location(
            id = 2L,
            title = "new location",
            tagRelations = tagRelations(TagRelation.Provides to newTag)
        )

        val state = mainFunctionalCore(
            state = gameState(
                locations = listOf(oldLocation, newLocation),
                locationSelectionState = locationSelectionState(selectedLocation = oldLocation),
                player = player(generalTags = listOf(oldTag, otherTag)),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SelectableClicked(id = newLocation.id)
        )

        assertThat(state)
            .prop(GameState::player)
            .prop(Player::generalTags)
            .containsExactly(otherTag, newTag)
    }

    @Test
    fun `should open finished screen if suspicion gained maximum value`() {
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(
                    action(id = 1L, ratioChanges = ratioChanges(RatioKey.Suspicion to 0.05))
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Suspicion, value = 0.95),
                ),
                currentScreen = Screen.Main,
                screenStack = listOf(Screen.GameStart),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(newState)
            .all {
                prop(GameState::currentEndingId).isEqualTo(0L)
                prop(GameState::currentScreen).isEqualTo(Screen.FinishedGame)
                prop(GameState::screenStack).isEmpty()
            }
    }

    @Test
    fun `should open finished screen if mutanity gained maximum value when devourer`() {
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(
                    action(id = 1L, ratioChanges = ratioChanges(RatioKey.Mutanity to 0.05))
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, value = 0.95),
                ),

                player = player(
                    traits = mapOf(
                        TraitId.Species to Species.Devourer
                    )
                ),
                currentScreen = Screen.Main,
                screenStack = listOf(Screen.GameStart),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(newState)
            .all {
                prop(GameState::currentEndingId).isEqualTo(1L)
                prop(GameState::currentScreen).isEqualTo(Screen.FinishedGame)
                prop(GameState::screenStack).isEmpty()
            }
    }

    @Test
    fun `should open finished screen if power gained maximum value when vampire`() {
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(
                    action(id = 1L, ratioChanges = ratioChanges(RatioKey.Power to 0.05))
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Power, value = 0.95),
                ),

                player = player(
                    traits = mapOf(
                        TraitId.Species to Species.Vampire
                    )
                ),
                currentScreen = Screen.Main,
                screenStack = listOf(Screen.GameStart),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(newState)
            .all {
                prop(GameState::currentEndingId).isEqualTo(1L)
                prop(GameState::currentScreen).isEqualTo(Screen.FinishedGame)
                prop(GameState::screenStack).isEmpty()
            }
    }

    @Test
    fun `should open finished screen if vampire clicked debug win action`() {
        val newState = mainFunctionalCore(
            state = gameState(
                actions = listOf(Actions.Win.copy(id = 1L)),
                ratios = listOf(
                    ratio(key = RatioKey.Power, value = 0.0),
                ),

                player = player(
                    traits = mapOf(
                        TraitId.Species to Species.Vampire
                    )
                ),
                currentScreen = Screen.Main,
                screenStack = listOf(Screen.GameStart),
                gameTitle = MenuTitleState.Result("Mutant Idle"),
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(newState)
            .all {
                prop(GameState::currentEndingId).isEqualTo(1L)
                prop(GameState::currentScreen).isEqualTo(Screen.FinishedGame)
                prop(GameState::screenStack).isEmpty()
            }
    }

    @Test
    fun `should use blood for invisibilty upgrade when you are a vampire`() {
        val state = mainFunctionalCore(
            state = gameState(
                upgrades = listOf(Upgrades.Invisibility.copy(id = 1L)),
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                player = player(
                    traits = mapOf(TraitId.Species to Species.Vampire)
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(state)
            .prop(GameState::upgrades)
            .extracting(Upgrade::id, Upgrade::status)
            .containsExactly(1L to UpgradeStatus.Bought)
    }

    @Test
    fun `should use scrap for invisibilty upgrade when you are an alien`() {
        val state = mainFunctionalCore(
            state = gameState(
                upgrades = listOf(Upgrades.Invisibility.copy(id = 1L)),
                resources = listOf(resource(key = ResourceKey.Scrap, value = 10.0)),
                player = player(
                    traits = mapOf(TraitId.Species to Species.Alien)
                ),
                gameTitle = MenuTitleState.Result("Mutant Idle")
            ),
            viewAction = MainViewAction.SelectableClicked(id = 1L)
        )

        assertThat(state)
            .prop(GameState::upgrades)
            .extracting(Upgrade::id, Upgrade::status)
            .containsExactly(1L to UpgradeStatus.Bought)
    }
}
