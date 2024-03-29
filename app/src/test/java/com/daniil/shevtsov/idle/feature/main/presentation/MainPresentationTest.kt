package com.daniil.shevtsov.idle.feature.main.presentation

import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.core.ui.Icons
import com.daniil.shevtsov.idle.feature.action.domain.Actions
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.action.domain.ratioChanges
import com.daniil.shevtsov.idle.feature.action.presentation.*
import com.daniil.shevtsov.idle.feature.coreshell.domain.gameState
import com.daniil.shevtsov.idle.feature.location.domain.location
import com.daniil.shevtsov.idle.feature.location.domain.locationSelectionState
import com.daniil.shevtsov.idle.feature.location.presentation.LocationModel
import com.daniil.shevtsov.idle.feature.location.presentation.LocationSelectionViewState
import com.daniil.shevtsov.idle.feature.player.core.domain.player
import com.daniil.shevtsov.idle.feature.player.species.domain.Species
import com.daniil.shevtsov.idle.feature.player.trait.domain.TraitId
import com.daniil.shevtsov.idle.feature.player.trait.domain.playerTrait
import com.daniil.shevtsov.idle.feature.plot.domain.PlotEntry
import com.daniil.shevtsov.idle.feature.plot.domain.plotEntry
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.presentation.RatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChange
import com.daniil.shevtsov.idle.feature.resource.domain.resourceChanges
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.UpgradesViewState
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tagRelations
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrades
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class MainPresentationTest {
    @Test
    fun `should form correct initial state`() = runBlockingTest {
        val state = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, name = "Blood", value = 10.0),
                resource(key = ResourceKey.Money, name = "Money", value = 20.0),
            ),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
            ),
            upgrades = listOf(
                upgrade(
                    id = 0L,
                    price = 32.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -32.0)
                ),
                upgrade(
                    id = 1L,
                    price = 35.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -25.0)
                ),
                upgrade(
                    id = 2L,
                    price = 150.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -150.0)
                ),
                upgrade(
                    id = 3L,
                    price = 30.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -30.0)
                ),
            ),
            plotEntries = listOf(
                plotEntry(text = "lol"),
                plotEntry(text = "kek"),
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
                    .extracting(RatioModel::title, RatioModel::percent)
                    .containsExactly("Mutanity" to 0.0, "Suspicion" to 0.0)

                extractingMainState().all {
                    extractingPlot()
                        .extractingEntries()
                        .isEqualTo(state.plotEntries)
                    prop(MainViewState.Success::shop)
                        .prop(UpgradesViewState::upgrades)
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
    fun `should display ratio label`() {
        val state = gameState(
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.105),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingRatios()
            .index(0)
            .all {
                prop(RatioModel::title).isEqualTo("Mutanity")
                prop(RatioModel::percent).isEqualTo(0.105)
                prop(RatioModel::percentLabel).isEqualTo("10.5 %")
            }
    }

    @Test
    fun `should only show resources that you have`() = runBlockingTest {
        val state = gameState(
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
    fun `should show correct icons for resources`() = runBlockingTest {
        val state = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 1.0),
                resource(key = ResourceKey.Money, value = 1.0),
                resource(key = ResourceKey.HumanFood, value = 1.0),
                resource(key = ResourceKey.Prisoner, value = 1.0),
                resource(key = ResourceKey.Remains, value = 1.0),
                resource(key = ResourceKey.FreshMeat, value = 1.0),
                resource(key = ResourceKey.Organs, value = 1.0),
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingResources()
            .extracting(ResourceModel::key, ResourceModel::icon)
            .containsExactly(
                ResourceKey.Blood to Icons.Blood,
                ResourceKey.Money to Icons.Money,
                ResourceKey.HumanFood to Icons.HumanFood,
                ResourceKey.Prisoner to Icons.Prisoner,
                ResourceKey.Remains to Icons.Remains,
                ResourceKey.FreshMeat to Icons.FreshMeat,
                ResourceKey.Organs to Icons.Organs,
            )
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        val state = gameState(
            resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            upgrades = listOf(
                upgrade(
                    id = 0L,
                    price = 5.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -5.0)
                )
            ),
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
            val state = gameState(
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                upgrades = listOf(
                    upgrade(
                        id = 0L,
                        price = 20.0,
                        resourceChanges = resourceChanges(ResourceKey.Blood to -20.0)
                    )
                ),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingUpgrades()
                .extracting(UpgradeModel::id, UpgradeModel::status)
                .containsExactly(0L to UpgradeStatusModel.NotAffordable)
        }

    @Test
    fun `should mark upgrade as bought if it is bought`() = runBlockingTest {
        val state = gameState(
            resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
            ),
            upgrades = listOf(
                upgrade(
                    id = 0L,
                    price = 10.0,
                    resourceChanges = resourceChanges(ResourceKey.Blood to -10.0),
                    status = UpgradeStatus.Bought
                )
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingUpgrades()
            .extracting(UpgradeModel::id, UpgradeModel::status)
            .containsExactly(0L to UpgradeStatusModel.Bought)
    }

    @Test
    fun `should show bought upgrades despite them providing tags that are already present`() =
        runBlockingTest {
            val presentTag = tag(name = "present tag")
            val state = gameState(
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                    ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
                ),
                player = player(generalTags = listOf(presentTag)),
                upgrades = listOf(
                    upgrade(
                        id = 0L,
                        price = 10.0,
                        resourceChanges = resourceChanges(ResourceKey.Blood to -10.0),
                        status = UpgradeStatus.Bought,
                        tagRelations = tagRelations(TagRelation.Provides to presentTag)
                    )
                ),
            )

            val viewState = mapMainViewState(state = state)

            assertThat(viewState)
                .extractingUpgrades()
                .extracting(UpgradeModel::id, UpgradeModel::status)
                .containsExactly(0L to UpgradeStatusModel.Bought)
        }

    @Test
    fun `should show correct ratio icons`() = runBlockingTest {
        val state = gameState(
            ratios = listOf(
                ratio(key = RatioKey.Mutanity),
                ratio(key = RatioKey.Suspicion),
            )
        )
        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingRatios()
            .extracting(RatioModel::key, RatioModel::icon)
            .containsExactly(
                RatioKey.Mutanity to Icons.Mutanity,
                RatioKey.Suspicion to Icons.Suspicion,
            )
    }

    @Test
    fun `should update mutant ratio names correctly`() = runBlockingTest {
        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.14)
                    )
                )
            )
        )
            .hasRatioName("Human")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.16)
                    )
                )
            )
        )
            .hasRatioName("Dormant")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.26)
                    )
                )
            )
        )
            .hasRatioName("Hidden")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Mutanity, value = 0.51)
                    )
                )
            )
        )
            .hasRatioName("Covert")

        assertThat(
            mapMainViewState(
                state = gameState(
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
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.14)
                    )
                )
            )
        )
            .hasRatioName("Unknown")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.16)
                    )
                )
            )
        )
            .hasRatioName("Rumors")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.26)
                    )
                )
            )
        )
            .hasRatioName("News")

        assertThat(
            mapMainViewState(
                state = gameState(
                    ratios = listOf(
                        ratio(key = RatioKey.Suspicion, value = 0.51)
                    )
                )
            )
        )
            .hasRatioName("Investigation")

        assertThat(
            mapMainViewState(
                state = gameState(
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
        val state = gameState(
            resources = listOf(
                resource(key = ResourceKey.Money, value = 35.0),
            ),
            actions = listOf(
                action(
                    id = 1L,
                    resourceChanges = resourceChanges(
                        ResourceKey.Money to -30.0,
                    ),
                ),
                action(
                    id = 2L,
                    resourceChanges = resourceChanges(
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
        val state = gameState(
            resources = listOf(
                resource(key = ResourceKey.Money, value = 35.0),
            ),
            actions = listOf(
                action(
                    id = 1L,
                    resourceChanges = resourceChanges(
                        ResourceKey.Money to -60.0,
                    ),
                ),
                action(
                    id = 2L,
                    resourceChanges = resourceChanges(
                        ResourceKey.Money to -10.0,
                    ),
                ),
                action(
                    id = 3L,
                    resourceChanges = resourceChanges(
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
    fun `should show correct icon for human and monster actions`() = runBlockingTest {
        val humanAction = action(
            id = 1L,
            tagRelations = tagRelations(TagRelation.RequiredAll to Tags.Form.Human)
        )
        val monsterAction = action(id = 2L)

        val state = gameState(
            actions = listOf(
                humanAction,
                monsterAction,
            ),
            player = player(generalTags = listOf(Tags.Form.Human)),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)
            .extracting(ActionModel::id, ActionModel::icon)
            .containsExactly(
                humanAction.id to ActionIcon(Icons.Human),
                monsterAction.id to ActionIcon(Icons.Monster),
            )
    }

    @Test
    fun `should display action resource changes`() = runBlockingTest {
        val action = action(
            id = 1L,
            resourceChanges = resourceChanges(
                ResourceKey.Blood to 10.0,
                ResourceKey.Money to -5.0,
            )
        )
        val state = gameState(
            resources = listOf(
                resource(key = ResourceKey.Blood, value = 1.0),
                resource(key = ResourceKey.Money, value = 5.0),
            ),
            actions = listOf(action),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingHumanActions()
            .index(0)
            .prop(ActionModel::resourceChanges)
            .extracting(ResourceChangeModel::icon, ResourceChangeModel::value)
            .containsExactly(
                Icons.Blood to "+10",
                Icons.Money to "-5",
            )
    }

    @Test
    fun `should display action ratio changes`() = runBlockingTest {
        val action = action(
            id = 1L,
            ratioChanges = ratioChanges(
                RatioKey.Mutanity to 0.5,
                RatioKey.Suspicion to -0.01,
            )
        )
        val state = gameState(
            ratios = listOf(
                ratio(key = RatioKey.Mutanity, value = 0.3),
                ratio(key = RatioKey.Suspicion, value = 0.5),
            ),
            actions = listOf(action),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingHumanActions()
            .index(0)
            .prop(ActionModel::ratioChanges)
            .extracting(RatioChangeModel::icon, RatioChangeModel::value)
            .containsExactly(
                Icons.Mutanity to "+50 %",
                Icons.Suspicion to "-1 %",
            )
    }

    @Test
    fun `should show locations if has any`() = runBlockingTest {
        val availableLocation = location(id = 1L)

        val state = gameState(
            locations = listOf(availableLocation),
            locationSelectionState = locationSelectionState(),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingAvailableLocations()
            .extracting(LocationModel::id)
            .containsExactly(availableLocation.id)
    }

    @Test
    fun `should show selected location info`() = runBlockingTest {
        val otherLocation = location(
            id = 1L,
            title = "other location",
            subtitle = "other location description",
        )
        val selectedLocation = location(
            id = 2,
            title = "selected location",
            subtitle = "selected location description",
        )

        val state = gameState(
            locations = listOf(otherLocation, selectedLocation),
            locationSelectionState = locationSelectionState(
                selectedLocation = selectedLocation,
            ),
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractSelectedLocation()
            .all {
                prop(LocationModel::id).isEqualTo(selectedLocation.id)
                prop(LocationModel::title).isEqualTo(selectedLocation.title)
            }
    }

    @Test
    fun `should only show main ratio beside suspicion`() {
        val mainRatio = RatioKey.ShipRepair
        val state = gameState(
            player = player(traits = mapOf(TraitId.Species to playerTrait(mainRatio = mainRatio))),
            ratios = listOf(
                ratio(key = RatioKey.Mutanity),
                ratio(key = RatioKey.ShipRepair),
                ratio(key = RatioKey.Suspicion),
            )
        )

        val viewState = mapMainViewState(state = state)

        assertThat(viewState)
            .extractingRatios()
            .extracting(RatioModel::key)
            .containsExactly(mainRatio, RatioKey.Suspicion)
    }

    @Test
    fun `should show resource changes of action`() {
        val blood = resource(key = ResourceKey.Blood, value = 10.0)
        val money = resource(key = ResourceKey.Money, value = 10.0)
        val state = gameState(
            resources = listOf(blood, money),
            actions = listOf(
                action(
                    resourceChanges = resourceChanges(
                        resourceChange(key = blood.key, change = 5.0),
                        resourceChange(key = money.key, change = -5.0),
                    )
                )
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingHumanActions()
            .extracting(ActionModel::resourceChanges)
            .index(0)
            .extracting(ResourceChangeModel::icon, ResourceChangeModel::value)
            .containsExactly(
                Icons.Blood to "+5",
                Icons.Money to "-5",
            )
    }

    @Test
    fun `should show ratio changes of action`() {
        val state = gameState(
            actions = listOf(
                action(
                    ratioChanges = ratioChanges(
                        RatioKey.Mutanity to -0.5,
                        RatioKey.Suspicion to 1.0,
                    )
                )
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingHumanActions()
            .extracting(ActionModel::ratioChanges)
            .index(0)
            .extracting(RatioChangeModel::icon, RatioChangeModel::value)
            .containsExactly(
                Icons.Mutanity to "-50 %",
                Icons.Suspicion to "+100 %",
            )
    }

    @Test
    fun `should show ratio changes of upgrade`() {
        val state = gameState(
            upgrades = listOf(
                upgrade(
                    ratioChanges = ratioChanges(
                        RatioKey.Mutanity to -0.5,
                        RatioKey.Suspicion to 1.0,
                    )
                )
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingUpgrades()
            .extracting(UpgradeModel::ratioChanges)
            .index(0)
            .extracting(RatioChangeModel::icon, RatioChangeModel::value)
            .containsExactly(
                Icons.Mutanity to "-50 %",
                Icons.Suspicion to "+100 %",
            )
    }

    @Test
    fun `should compact identical plot entries`() {
        val state = gameState(
            plotEntries = listOf(
                plotEntry(text = "kek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "lol"),
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingPlot()
            .extracting(PlotEntry::text)
            .containsExactly(
                "kek",
                "cheburek (x4)",
                "lol",
            )
    }

    @Test
    fun `should compact two different identical plot entries`() {
        val state = gameState(
            plotEntries = listOf(
                plotEntry(text = "kek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "lol"),
                plotEntry(text = "cheburek2"),
                plotEntry(text = "cheburek2"),
                plotEntry(text = "cheburek2"),
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingPlot()
            .extracting(PlotEntry::text)
            .containsExactly(
                "kek",
                "cheburek (x4)",
                "lol",
                "cheburek2 (x3)",
            )
    }

    @Test
    fun `should compact same two identical plot entries in different places`() {
        val state = gameState(
            plotEntries = listOf(
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "kek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "cheburek"),
                plotEntry(text = "lol"),
            )
        )
        val viewState = mapMainViewState(state)

        assertThat(viewState)
            .extractingPlot()
            .extracting(PlotEntry::text)
            .containsExactly(
                "cheburek (x2)",
                "kek",
                "cheburek (x4)",
                "lol",
            )
    }

    @Test
    fun `should display main resource as blood for vampire`() {
        val viewState = mapMainViewState(
            gameState(
                player = player(
                    traits = mapOf(TraitId.Species to Species.Vampire)
                ),
                upgrades = listOf(Upgrades.Invisibility.copy(id = 1L))
            )
        )

        assertThat(viewState)
            .extractingUpgrades()
            .index(0)
            .prop(UpgradeModel::resourceChanges)
            .extracting(ResourceChangeModel::icon)
            .containsExactly(Icons.Blood)
    }

    @Test
    fun `should display appearance change action when has required tags`() {
        val viewState = mapMainViewState(
            gameState(
                player = player(
                    traits = mapOf(TraitId.Species to Species.Shapeshifter),
                    generalTags = listOf(Tags.Abilities.AppearanceChange)
                ),
                resources = listOf(resource(key = ResourceKey.DNA, value = 100.0)),
                actions = listOf(Actions.AppearanceChange.copy(id = 1L))
            )
        )

        assertThat(viewState)
            .extractingHumanActions()
            .index(0)
            .prop(ActionModel::id)
            .isEqualTo(1L)
    }

    @Test
    fun `should display main resource as scrap for alien`() {
        val viewState = mapMainViewState(
            gameState(
                player = player(
                    traits = mapOf(TraitId.Species to Species.Alien)
                ),
                upgrades = listOf(Upgrades.Invisibility.copy(id = 1L))
            )
        )

        assertThat(viewState)
            .extractingUpgrades()
            .index(0)
            .prop(UpgradeModel::resourceChanges)
            .extracting(ResourceChangeModel::icon)
            .containsExactly(Icons.Scrap)
    }

    @Test
    fun `should display main ratio as Power for vampire`() {
        val viewState = mapMainViewState(
            gameState(
                player = player(
                    traits = mapOf(TraitId.Species to Species.Vampire)
                ),
                actions = listOf(Actions.Win.copy(id = 1L))
            )
        )

        assertThat(viewState)
            .extractingHumanActions()
            .index(0)
            .prop(ActionModel::ratioChanges)
            .extracting(RatioChangeModel::icon)
            .containsExactly(Icons.Power)
    }

    @Test
    fun `should display main ratio as Ship Repair for alien`() {
        val viewState = mapMainViewState(
            gameState(
                player = player(
                    traits = mapOf(TraitId.Species to Species.Alien)
                ),
                actions = listOf(Actions.Win.copy(id = 1L))
            )
        )

        assertThat(viewState)
            .extractingHumanActions()
            .index(0)
            .prop(ActionModel::ratioChanges)
            .extracting(RatioChangeModel::icon)
            .containsExactly(Icons.ShipRepair)
    }


    private fun Assert<MainViewState>.extractingPlot() = extractingMainState()
        .prop(MainViewState.Success::plotEntries)

    private fun Assert<List<PlotEntry>>.extractingEntries() = this

    fun Assert<MainViewState>.extractingUpgrades() =
        extractingMainState()
            .prop(MainViewState.Success::shop)
            .prop(UpgradesViewState::upgrades)

    private fun Assert<MainViewState>.extractingLocationSelectionViewState() =
        extractingMainState()
            .prop(MainViewState.Success::locationSelectionViewState)

    private fun Assert<MainViewState>.extractingAvailableLocations() =
        extractingLocationSelectionViewState()
            .prop(LocationSelectionViewState::locations)

    private fun Assert<MainViewState>.extractSelectedLocation() =
        extractingLocationSelectionViewState()
            .prop(LocationSelectionViewState::selectedLocation)

    private fun Assert<MainViewState>.extractingHumanActions() =
        extractingMainState()
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)

    private fun Assert<MainViewState>.extractingRatios() = extractingMainState()
        .prop(MainViewState.Success::ratios)

    private fun Assert<MainViewState>.extractingMutanityName() =
        extractingMainState()
            .prop(MainViewState.Success::ratios)
            .index(0)
            .prop(RatioModel::name)


    private fun Assert<MainViewState>.hasRatioName(expectedName: String) =
        extractingMutanityName()
            .isEqualTo(expectedName)

    private fun Assert<MainViewState>.extractingResources() =
        extractingMainState()
            .prop(MainViewState.Success::resources)

    private fun Assert<MainViewState>.extractingResourceNameAndValues() =
        extractingResources()
            .extracting(ResourceModel::name, ResourceModel::value)
}

fun Assert<MainViewState>.extractingMainState() =
    isInstanceOf(MainViewState.Success::class)
