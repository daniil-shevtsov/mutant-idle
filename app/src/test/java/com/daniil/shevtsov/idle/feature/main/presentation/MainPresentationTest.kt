package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.idle.feature.flavor.Flavors
import com.daniil.shevtsov.idle.feature.flavor.flavor
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationModel
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.player.species.domain.playerSpecies
import com.daniil.shevtsov.idle.feature.player.species.presentation.PlayerSpeciesModel
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class MainPresentationTest {
    @Test
    fun `should form correct initial state`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Blood, name = "Blood", value = 10.0),
                resource(key = ResourceKey.Money, name = "Money", value = 20.0),
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
            ),
            upgrades = listOf(
                upgrade(id = 0L, price = 32.0),
                upgrade(id = 1L, price = 35.0),
                upgrade(id = 2L, price = 150.0),
                upgrade(id = 3L, price = 30.0),
            ),
            actions = listOf(
                action(id = 0L, title = "human action"),
                action(id = 1L, title = "mutant action"),
            ),
            sections = listOf(
                sectionState(key = SectionKey.Resources, isCollapsed = false),
                sectionState(key = SectionKey.Actions, isCollapsed = false),
                sectionState(key = SectionKey.Upgrades, isCollapsed = false),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .all {
                extractingResourceNameAndValues()
                    .containsExactly("Blood" to "10", "Money" to "20")
                extractingRatios()
                    .containsExactly("Mutanity" to 0.0, "Suspicion" to 0.0)

                extractingMainState().all {
                    prop(MainViewState.Success::shop)
                        .prop(ShopState::upgradeLists)
                        .index(0)
                        .extracting(UpgradeModel::id)
                        .containsExactly(0L, 1L, 2L, 3L)
                    prop(MainViewState.Success::actionState)
                        .prop(ActionsState::actionPanes)
                        .index(0)
                        .prop(ActionPane::actions)
                        .extracting(ActionModel::title)
                        .containsExactly("human action", "mutant action")
                    prop(MainViewState.Success::sectionCollapse)
                        .containsOnly(
                            SectionKey.Resources to false,
                            SectionKey.Actions to false,
                            SectionKey.Upgrades to false,
                        )
                }
            }
    }

    @Test
    fun `should only show resources that you have`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Blood, name = "Blood", value = 10.0),
                resource(key = ResourceKey.Money, name = "Money", value = 20.0),
                resource(key = ResourceKey.Prisoner, name = "Prisoners", value = 0.0),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingResources()
            .extracting(ResourceModel::name)
            .containsExactly("Blood", "Money")
    }

    @Test
    fun `should only show upgrades if you have all requiredAll or at least one requiredAny tag`() =
        runBlockingTest {
            val availableTag = tag(name = "lol")
            val unavailableTag = tag(name = "kek")

            val requiredAllAvailableUpgrade = upgrade(
                id = 1L,
                tags = mapOf(TagRelation.RequiredAll to listOf(availableTag)),
            )
            val requiredAnyAvailableUpgrade = upgrade(
                id = 2L,
                tags = mapOf(TagRelation.RequiredAny to listOf(availableTag, unavailableTag)),
            )
            val expectedAvailableUpgrades = listOf(
                requiredAllAvailableUpgrade,
                requiredAnyAvailableUpgrade,
            )
            val expectedUnavailableUpgrades = listOf(
                upgrade(
                    id = 3L,
                    tags = mapOf(TagRelation.RequiredAll to listOf(availableTag, unavailableTag)),
                ),
                upgrade(
                    id = 4L, tags = mapOf(TagRelation.RequiredAll to listOf(unavailableTag)),
                )
            )

            val state = mainFunctionalCoreState(
                upgrades = expectedAvailableUpgrades + expectedUnavailableUpgrades,
                player = player(generalTags = listOf(availableTag)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingUpgrades()
                .extracting(UpgradeModel::id)
                .containsExactly(requiredAllAvailableUpgrade.id, requiredAnyAvailableUpgrade.id)
        }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            upgrades = listOf(upgrade(id = 0L, price = 5.0)),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingUpgrades()
            .extracting(UpgradeModel::id, UpgradeModel::status)
            .containsExactly(0L to UpgradeStatusModel.Affordable)
    }

    @Test
    fun `should mark upgrade as not affordable if its price higher than resource`() =
        runBlockingTest {
            val state = mainFunctionalCoreState(
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                upgrades = listOf(upgrade(id = 0L, price = 20.0)),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingUpgrades()
                .extracting(UpgradeModel::id, UpgradeModel::status)
                .containsExactly(0L to UpgradeStatusModel.NotAffordable)
        }

    @Test
    fun `should mark upgrade as bought if it is bought`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
            ),
            upgrades = listOf(upgrade(id = 0L, price = 10.0, status = UpgradeStatus.Bought)),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingUpgrades()
            .extracting(UpgradeModel::id, UpgradeModel::status)
            .containsExactly(0L to UpgradeStatusModel.Bought)
    }

    @Test
    fun `should update mutant ratio names correctly`() = runBlockingTest {
        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.14)
                    )
                )
            )
        )
            .hasRatioName("Human")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.16)
                    )
                )
            )
        )
            .hasRatioName("Dormant")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.26)
                    )
                )
            )
        )
            .hasRatioName("Hidden")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.51)
                    )
                )
            )
        )
            .hasRatioName("Covert")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.81)
                    )
                )
            )
        )
            .hasRatioName("Honest")
    }

    @Test
    fun `should update suspicion ratio names correctly`() = runBlockingTest {
        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.14)
                    )
                )
            )
        )
            .hasRatioName("Unknown")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.16)
                    )
                )
            )
        )
            .hasRatioName("Rumors")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.26)
                    )
                )
            )
        )
            .hasRatioName("News")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.51)
                    )
                )
            )
        )
            .hasRatioName("Investigation")

        assertThat(
            mapMainViewState(
                state = mainFunctionalCoreState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.81)
                    )
                )
            )
        )
            .hasRatioName("Manhunt")
    }


    @Test
    fun `should hide actions if it requires not available resources`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Money, value = 35.0),
            ),
            actions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -30.0,
                    ),
                ),
                action(
                    id = 2L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -50.0,
                    ),
                ),
            ),
        )
        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingHumanActions()
            .extracting(ActionModel::id, ActionModel::isEnabled)
            .containsExactly(
                1L to true,
            )
    }

    @Disabled("Need to figure out which actions to hide and which to show disabled")
    @Test
    fun `show enabled actions before disabled if got both`() = runBlockingTest {
        val state = mainFunctionalCoreState(
            resources = listOf(
                resource(key = ResourceKey.Money, value = 35.0),
            ),
            actions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -60.0,
                    ),
                ),
                action(
                    id = 2L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -10.0,
                    ),
                ),
                action(
                    id = 3L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -50.0,
                    ),
                ),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingHumanActions()
            .extracting(ActionModel::id)
            .containsExactly(2L, 1L, 3L)
    }

    @Test
    fun `should show debug job selection if has any`() = runBlockingTest {
        val availableJobs = listOf(
            playerJob(id = 0L),
            playerJob(id = 1L),
            playerJob(id = 2L),
        )

        val state = mainFunctionalCoreState(
            drawerTabs = listOf(drawerTab(id = DrawerTabId.Debug, isSelected = true)),
            availableJobs = availableJobs,
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingDebugState()
            .extractingJobSelection()
            .extracting(PlayerJobModel::id)
            .containsExactly(0L, 1L, 2L)
    }

    @Test
    fun `should show debug species selection if has any`() = runBlockingTest {
        val availableSpecies = listOf(
            playerSpecies(id = 0L),
            playerSpecies(id = 1L),
            playerSpecies(id = 2L),
        )

        val state = mainFunctionalCoreState(
            drawerTabs = listOf(drawerTab(id = DrawerTabId.Debug, isSelected = true)),
            availableSpecies = availableSpecies,
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingDebugState()
            .extractingSpeciesSelection()
            .extracting(PlayerSpeciesModel::id)
            .containsExactly(0L, 1L, 2L)
    }

    @Test
    fun `should show only actions that require avialable tags`() = runBlockingTest {
        val availableTag = tag(name = "lol")
        val notAvailableTag = tag(name = "kek")

        val availableAction = action(
            id = 1L,
            tags = mapOf(TagRelation.RequiredAll to listOf(availableTag))
        )
        val notAvailableAction = action(
            id = 2L,
            tags = mapOf(TagRelation.RequiredAll to listOf(availableTag, notAvailableTag))
        )

        val state = mainFunctionalCoreState(
            actions = listOf(
                availableAction,
                notAvailableAction,
            ),
            player = player(
                generalTags = listOf(
                    availableTag
                )
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)
            .extracting(ActionModel::id)
            .containsExactly(availableAction.id)
    }

    @Test
    fun `should handle requiredAny tag relation for actions`() = runBlockingTest {
        val availableTag = tag(name = "available")
        val oneTagOption = tag(name = "required tag option 1")
        val anotherTagOption = tag(name = "required tag option 2")
        val notAvailableTag = tag(name = "not available tag")

        val availableAction = action(
            id = 1L,
            tags = mapOf(
                TagRelation.RequiredAll to listOf(availableTag),
                TagRelation.RequiredAny to listOf(oneTagOption, anotherTagOption)
            )
        )
        val notAvailableAction = action(
            id = 2L,
            tags = mapOf(
                TagRelation.RequiredAll to listOf(notAvailableTag),
            )
        )

        val state = mainFunctionalCoreState(
            actions = listOf(
                availableAction,
                notAvailableAction,
            ),
            player = player(
                generalTags = listOf(
                    availableTag,
                    oneTagOption,
                )
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)
            .extracting(ActionModel::id)
            .containsExactly(availableAction.id)

        val anotherState = mainFunctionalCoreState(
            actions = listOf(
                availableAction,
                notAvailableAction,
            ),
            player = player(
                generalTags = listOf(
                    availableTag,
                    anotherTagOption,
                )
            ),
        )

        val anotherViewState = mapMainViewState(state = anotherState)

        assertThat(anotherViewState)
            .extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)
            .extracting(ActionModel::id)
            .containsExactly(availableAction.id)
    }

    @Test
    fun `should handle requiredNone tag relation for actions`() = runBlockingTest {
        val availableTag = tag(name = "available")
        val forbiddenTag = tag(name = "forbidden")

        val availableAction = action(
            id = 1L,
            tags = mapOf(TagRelation.RequiredAll to listOf(availableTag))
        )
        val notAvailableAction = action(
            id = 2L,
            tags = mapOf(
                TagRelation.RequiredAll to listOf(availableTag),
                TagRelation.RequiresNone to listOf(forbiddenTag),
            )
        )

        val state = mainFunctionalCoreState(
            actions = listOf(
                availableAction,
                notAvailableAction,
            ),
            player = player(
                generalTags = listOf(
                    availableTag,
                    forbiddenTag,
                )
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)
            .extracting(ActionModel::id)
            .containsExactly(availableAction.id)
    }

    @Test
    fun `should replace placeholders in action description if has any`() = runBlockingTest {
        val tag = tag(name = "flavor tag")
        val flavoredDescription = "flavored"
        val flavor = flavor(
            placeholder = "${Flavors.PREFIX}placeholder",
            values = mapOf(tag to flavoredDescription),
        )

        val state = mainFunctionalCoreState(
            actions = listOf(
                action(subtitle = flavor.placeholder)
            ),
            flavors = listOf(flavor),
            player = player(
                generalTags = listOf(tag)
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingHumanActions()
            .extracting(ActionModel::subtitle)
            .containsExactly(flavoredDescription)
    }

    @Test
    fun `should replace placeholders in upgrade description if has any`() = runBlockingTest {
        val tag = tag(name = "flavor tag")
        val flavoredDescription = "flavored"
        val flavor = flavor(
            placeholder = "${Flavors.PREFIX}placeholder",
            values = mapOf(tag to flavoredDescription),
        )

        val state = mainFunctionalCoreState(
            upgrades = listOf(
                upgrade(subtitle = flavor.placeholder)
            ),
            flavors = listOf(flavor),
            player = player(
                generalTags = listOf(tag)
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingUpgrades()
            .extracting(UpgradeModel::subtitle)
            .containsExactly(flavoredDescription)
    }

    @Test
    fun `should show locations if has any`() = runBlockingTest {
        val availableLocation = location(id = 1L)

        val state = mainFunctionalCoreState(
            locationSelectionState = locationSelectionState(
                allLocations = listOf(availableLocation),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingAvailableLocations()
            .extracting(LocationModel::id)
            .containsExactly(availableLocation.id)
    }

    @Test
    fun `should not show locations that require not available tags`() = runBlockingTest {
        val availableTag = tag(name = "available tag")
        val unavailableTag = tag(name = "unavailable tag")
        val availableLocation = location(
            id = 1L,
            title = "available location",
            tags = mapOf(TagRelation.RequiredAll to listOf(availableTag))
        )
        val unavailableLocation = location(
            id = 2,
            title = "unavailable location",
            tags = mapOf(TagRelation.RequiredAll to listOf(unavailableTag))
        )

        val state = mainFunctionalCoreState(
            player = player(generalTags = listOf(availableTag)),
            locationSelectionState = locationSelectionState(
                allLocations = listOf(availableLocation, unavailableLocation),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingAvailableLocations()
            .extracting(LocationModel::title)
            .containsExactly(availableLocation.title)
    }

    private fun Assert<MainViewState>.extractingUpgrades() =
        extractingMainState()
            .prop(MainViewState.Success::shop)
            .prop(ShopState::upgradeLists)
            .index(0)

    private fun Assert<MainViewState>.extractingLocationSelectionViewState() =
        extractingMainState()
            .prop(MainViewState.Success::locationSelectionViewState)

    private fun Assert<MainViewState>.extractingAvailableLocations() =
        extractingLocationSelectionViewState()
            .prop(LocationSelectionViewState::locations)

    private fun Assert<MainViewState>.extractingHumanActions() =
        extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)

    private fun Assert<HumanityRatioModel>.assertPercentage(expected: Double) =
        prop(HumanityRatioModel::percent)
            .isCloseTo(expected, 0.00001)

    private fun Assert<MainViewState>.extractingMutanity() =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(0)

    private fun Assert<MainViewState>.extractingSuspicion() =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(1)

    private fun Assert<MainViewState>.extractingMutanityValue() =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(0)
            .prop(HumanityRatioModel::percent)

    private fun Assert<MainViewState>.extractingMutanityName() =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(0)
            .prop(HumanityRatioModel::name)

    private fun Assert<MainViewState>.extractingRatios() = extractingMainState()
        .prop(MainViewState.Success::ratios)
        .extracting(HumanityRatioModel::title, HumanityRatioModel::percent)


    private fun Assert<MainViewState>.hasRatioName(expectedName: String) =
        extractingMutanityName()
            .isEqualTo(expectedName)

    private fun Assert<MainViewState>.hasSuspicionRatioName(expectedName: String) =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(1)
            .prop(HumanityRatioModel::name)
            .isEqualTo(expectedName)

    private fun Assert<MainViewState>.extractingResources() =
        extractingMainState()
            .prop(MainViewState.Success::resources)

    private fun Assert<MainViewState>.extractingResourceNameAndValues() =
        extractingResources()
            .extracting(ResourceModel::name, ResourceModel::value)

    private fun Assert<MainViewState>.extractingBlood() =
        extractingMainState()
            .prop(MainViewState.Success::resources)
            .index(0)
            .prop(ResourceModel::value)

    private fun Assert<MainViewState>.extractingMoney() = extractingMainState()
        .prop(MainViewState.Success::resources)
        .index(1)
        .prop(ResourceModel::value)

    private fun Assert<MainViewState>.extractingDebugState() = extractingMainState()
        .prop(MainViewState.Success::drawerState)
        .prop(DrawerViewState::drawerContent)
        .isInstanceOf(DrawerContentViewState.Debug::class)
        .prop(DrawerContentViewState.Debug::state)

    private fun Assert<DebugViewState>.extractingJobSelection() = prop(DebugViewState::jobSelection)
    private fun Assert<DebugViewState>.extractingSpeciesSelection() =
        prop(DebugViewState::speciesSelection)

    private fun Assert<MainViewState>.extractingMainState() =
        isInstanceOf(MainViewState.Success::class)
}
