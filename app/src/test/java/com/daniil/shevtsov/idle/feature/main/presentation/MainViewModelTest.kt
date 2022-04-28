package com.daniil.shevtsov.idle.feature.main.presentation

import app.cash.turbine.test
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.action.domain.action
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.debug.presentation.DebugViewState
import com.daniil.shevtsov.idle.feature.drawer.presentation.DrawerTabId
import com.daniil.shevtsov.idle.feature.drawer.presentation.drawerTab
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.main.domain.mainFunctionalCoreState
import com.daniil.shevtsov.idle.feature.player.info.presentation.PlayerInfoState
import com.daniil.shevtsov.idle.feature.player.job.domain.playerJob
import com.daniil.shevtsov.idle.feature.player.job.presentation.PlayerJobModel
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.domain.ratio
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.domain.resource
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.tagsystem.domain.TagRelation
import com.daniil.shevtsov.idle.feature.tagsystem.domain.Tags
import com.daniil.shevtsov.idle.feature.tagsystem.domain.tag
import com.daniil.shevtsov.idle.feature.upgrade.domain.upgrade
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.player
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class MainViewModelTest {

    private val imperativeShell = MainImperativeShell(initialState = mainFunctionalCoreState())

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @Test
    fun `should form correct initial state`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                upgrades = listOf(
                    upgrade(id = 0L, price = 32.0),
                    upgrade(id = 1L, price = 35.0),
                    upgrade(id = 2L, price = 150.0),
                    upgrade(id = 3L, price = 30.0),
                ),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                    ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
                ),
                actions = listOf(
                    action(id = 0L, title = "human action"),
                    action(id = 1L, title = "mutant action"),
                ),
                resources = listOf(
                    resource(key = ResourceKey.Blood, name = "Blood", value = 10.0),
                    resource(key = ResourceKey.Money, name = "Money", value = 20.0),
                ),
                sections = listOf(
                    sectionState(key = SectionKey.Resources, isCollapsed = false),
                    sectionState(key = SectionKey.Actions, isCollapsed = false),
                    sectionState(key = SectionKey.Upgrades, isCollapsed = false),
                )
            )
        )


        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    extractingResourceNameAndValues()
                        .containsExactly("Blood" to "10", "Money" to "20")
                    extractingRatios()
                        .containsExactly("Human" to 0.0, "Unknown" to 0.0)
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
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                resources = listOf(
                    resource(key = ResourceKey.Blood, name = "Blood", value = 10.0),
                    resource(key = ResourceKey.Money, name = "Money", value = 20.0),
                    resource(key = ResourceKey.Prisoner, name = "Prisoners", value = 0.0),
                )
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingResources()
                .extracting(ResourceModel::name)
                .containsExactly("Blood", "Money")
        }
    }

    @Test
    fun `should only show upgrades if you have all required tags`() = runBlockingTest {
        val availableTag = tag(name = "lol")
        val unavailableTag = tag(name = "kek")

        val expectedAvailableUpgrade =
            upgrade(id = 1L, tags = mapOf(availableTag to TagRelation.RequiredAll))

        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                player = player(tags = listOf(availableTag)),
                upgrades = listOf(
                    expectedAvailableUpgrade,
                    upgrade(
                        id = 2L,
                        tags = mapOf(
                            availableTag to TagRelation.RequiredAll,
                            unavailableTag to TagRelation.RequiredAll
                        )
                    ),
                    upgrade(id = 3L, tags = mapOf(unavailableTag to TagRelation.RequiredAll)),
                )
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingUpgrades()
                .extracting(UpgradeModel::id)
                .containsExactly(expectedAvailableUpgrade.id)
        }
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                upgrades = listOf(upgrade(id = 0L, price = 5.0)),
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            )
        )

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .extractingUpgrades()
                .extracting(UpgradeModel::id, UpgradeModel::status)
                .containsExactly(0L to UpgradeStatusModel.Affordable)
        }
    }

    @Test
    fun `should mark upgrade as not affordable if its price higher than resource`() =
        runBlockingTest {
            imperativeShell.updateState(
                newState = mainFunctionalCoreState(
                    upgrades = listOf(upgrade(id = 0L, price = 20.0)),
                    resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                )
            )

            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state)
                    .extractingUpgrades()
                    .extracting(UpgradeModel::id, UpgradeModel::status)
                    .containsExactly(0L to UpgradeStatusModel.NotAffordable)
            }
        }

    @Test
    fun `should mark upgrade as bought if it is bought`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                upgrades = listOf(upgrade(id = 0L, price = 10.0)),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                    ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
                ),
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
            )
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingUpgrades()
                .extracting(UpgradeModel::id, UpgradeModel::status)
                .containsExactly(0L to UpgradeStatusModel.Bought)
        }
    }

    @Test
    fun `should update human ratio after upgrade bought`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                balanceConfig = balanceConfig(
                    resourceSpentForFullMutant = 100.0,
                ),
                upgrades = listOf(upgrade(id = 0L, price = 10.0)),
                resources = listOf(resource(key = ResourceKey.Blood, value = 10.0)),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity)
                )
            )
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .extractingMutanityValue()
                .isEqualTo(0.10)
        }
    }

    @Test
    fun `should update mutant ratio names correctly`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                balanceConfig = balanceConfig(
                    resourceSpentForFullMutant = 100.0,
                ),
                upgrades = listOf(
                    upgrade(id = 0L, price = 15.0),
                    upgrade(id = 1L, price = 10.0),
                    upgrade(id = 2L, price = 25.0),
                    upgrade(id = 3L, price = 30.0),
                ),
                resources = listOf(resource(key = ResourceKey.Blood, value = 1000.0)),
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, value = 0.0)
                )
            )
        )

        viewModel.state.test {
            assertThat(awaitItem()).hasRatioName("Human")

            viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))
            assertThat(expectMostRecentItem()).hasRatioName("Dormant")

            viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))
            assertThat(expectMostRecentItem()).hasRatioName("Hidden")

            viewModel.handleAction(MainViewAction.UpgradeSelected(id = 2L))
            assertThat(expectMostRecentItem()).hasRatioName("Covert")

            viewModel.handleAction(MainViewAction.UpgradeSelected(id = 3L))
            assertThat(expectMostRecentItem()).hasRatioName("Honest")
        }
    }

    @Test
    fun `should update suspicion ratio names correctly`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                    ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
                ),
                actions = listOf(
                    action(id = 0L, ratioChanges = mapOf(RatioKey.Suspicion to 0.15f)),
                    action(id = 1L, ratioChanges = mapOf(RatioKey.Suspicion to 0.10f)),
                    action(id = 2L, ratioChanges = mapOf(RatioKey.Suspicion to 0.25f)),
                    action(id = 3L, ratioChanges = mapOf(RatioKey.Suspicion to 0.30f)),
                ),
            )
        )

        viewModel.state.test {
            assertThat(awaitItem()).hasSuspicionRatioName("Unknown")

            viewModel.handleAction(MainViewAction.ActionClicked(id = 0L))
            assertThat(expectMostRecentItem()).hasSuspicionRatioName("Rumors")

            viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))
            assertThat(expectMostRecentItem()).hasSuspicionRatioName("News")

            viewModel.handleAction(MainViewAction.ActionClicked(id = 2L))
            assertThat(expectMostRecentItem()).hasSuspicionRatioName("Investigation")

            viewModel.handleAction(MainViewAction.ActionClicked(id = 3L))
            assertThat(expectMostRecentItem()).hasSuspicionRatioName("Manhunt")
        }
    }

    @Test
    fun `should update both ratios correctly when actions change them both`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                ratios = listOf(
                    ratio(key = RatioKey.Mutanity, title = "Mutanity", value = 0.0),
                    ratio(key = RatioKey.Suspicion, title = "Suspicion", value = 0.0),
                ),
                actions = listOf(
                    action(id = 0L, ratioChanges = mapOf(RatioKey.Suspicion to 0.15f)),
                    action(id = 1L, ratioChanges = mapOf(RatioKey.Mutanity to 0.25f)),
                ),
            )
        )

        viewModel.state.test {
            viewModel.handleAction(MainViewAction.ActionClicked(id = 0L))
            assertThat(expectMostRecentItem()).all {
                extractingSuspicion().assertPercentage(0.15)
                extractingMutanity().assertPercentage(0.0)
            }

            viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))
            assertThat(expectMostRecentItem()).all {
                extractingSuspicion().assertPercentage(0.15)
                extractingMutanity().assertPercentage(0.25)
            }
        }
    }

    @Test
    fun `should update blood when action clicked`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                actions = listOf(
                    action(id = 1L, resourceChanges = mapOf(ResourceKey.Blood to 50.0))
                ),
                resources = listOf(resource(key = ResourceKey.Blood, value = 1000.0)),
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingBlood()
                .isEqualTo("1000")

            viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

            assertThat(expectMostRecentItem())
                .extractingBlood()
                .isEqualTo("1050")
        }
    }

    @Test
    fun `should update both resources when action with several changes clicked`() =
        runBlockingTest {
            imperativeShell.updateState(
                newState = mainFunctionalCoreState(
                    actions = listOf(
                        action(
                            id = 1L,
                            resourceChanges = mapOf(
                                ResourceKey.Blood to 50.0,
                                ResourceKey.Money to -30.0,
                            ),
                        )
                    ),
                    resources = listOf(
                        resource(key = ResourceKey.Blood, name = "Blood", value = 1000.0),
                        resource(key = ResourceKey.Money, name = "Money", value = 500.0),
                    ),
                )
            )

            viewModel.state.test {
                assertThat(expectMostRecentItem())
                    .extractingResourceNameAndValues()
                    .containsExactly("Blood" to "1000", "Money" to "500")

                viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

                assertThat(expectMostRecentItem())
                    .extractingResourceNameAndValues()
                    .containsExactly("Blood" to "1050", "Money" to "470")
            }
        }

    @Test
    fun `should toggle corresponding collapse state when toggle clicked`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                sections = listOf(sectionState(key = SectionKey.Resources, isCollapsed = false))
            )
        )

        viewModel.state.test {
            viewModel.handleAction(MainViewAction.ToggleSectionCollapse(key = SectionKey.Resources))

            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::sectionCollapse)
                .contains(SectionKey.Resources to true)

            viewModel.handleAction(MainViewAction.ToggleSectionCollapse(key = SectionKey.Resources))

            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::sectionCollapse)
                .contains(SectionKey.Resources to false)
        }
    }

    @Test
    fun `should not apply action if it requires unavailable resources`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                actions = listOf(
                    action(
                        id = 1L,
                        resourceChanges = mapOf(ResourceKey.Money to -30.0),
                    )
                ),
                resources = listOf(resource(key = ResourceKey.Money, value = 10.0)),
            )
        )

        viewModel.state.test {
            viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::resources)
                .extracting(ResourceModel::value)
                .containsExactly("10")
        }
    }

    @Test
    fun `should not apply action if it requires both available and not available resources`() =
        runBlockingTest {
            imperativeShell.updateState(
                newState = mainFunctionalCoreState(
                    actions = listOf(
                        action(
                            id = 1L,
                            resourceChanges = mapOf(
                                ResourceKey.Money to -30.0,
                                ResourceKey.Blood to -50.0,
                            ),
                        )
                    ),
                    resources = listOf(
                        resource(key = ResourceKey.Blood, value = 30.0),
                        resource(key = ResourceKey.Money, value = 40.0),
                    ),
                )
            )

            viewModel.state.test {
                viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

                assertThat(expectMostRecentItem())
                    .all {
                        extractingMoney().isEqualTo("40")
                        extractingBlood().isEqualTo("30")
                    }
            }
        }

    @Test
    fun `should disable actions if it requires not available resources`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
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
                    )
                ),
                resources = listOf(
                    resource(key = ResourceKey.Money, value = 35.0),
                ),
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingHumanActions()
                .extracting(ActionModel::id, ActionModel::isEnabled)
                .containsExactly(
                    1L to true,
                    2L to false,
                )
        }
    }

    @Test
    fun `show enabled actions before disabled if got both`() = runBlockingTest {
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
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
                resources = listOf(
                    resource(key = ResourceKey.Money, value = 35.0),
                ),
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingHumanActions()
                .extracting(ActionModel::id)
                .containsExactly(2L, 1L, 3L)
        }
    }

    @Test
    fun `should show debug job selection if has any`() = runBlockingTest {
        val availableJobs = listOf(
            playerJob(id = 0L),
            playerJob(id = 1L),
            playerJob(id = 2L),
        )

        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                availableJobs = availableJobs,
                drawerTabs = listOf(drawerTab(id = DrawerTabId.Debug, isSelected = true)),
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingDebugState()
                .extractingJobSelection()
                .extracting(PlayerJobModel::id)
                .containsExactly(0L, 1L, 2L)
        }
    }

    @Test
    fun `should add job tags to player when job selected`() = runBlockingTest {
        val job = playerJob(
            tags = listOf(
                tag(name = "lol"),
                tag(name = "kek"),
            )
        )

        val jobTags = job.tags
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                availableJobs = listOf(job),
                drawerTabs = listOf(drawerTab(id = DrawerTabId.PlayerInfo, isSelected = true)),
            )
        )
        viewModel.handleAction(MainViewAction.DebugJobSelected(id = job.id))

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::drawerState)
                .prop(DrawerViewState::drawerContent)
                .isInstanceOf(DrawerContentViewState.PlayerInfo::class)
                .prop(DrawerContentViewState.PlayerInfo::playerInfo)
                .prop(PlayerInfoState::playerTags)
                .containsSubList(jobTags)
        }
    }

    @Test
    fun `should remove previous job tags if job changed`() = runBlockingTest {
        val previousJob = playerJob(
            id = 0L,
            tags = listOf(
                Tags.MeatAccess,
                Tags.SocialJob,
            )
        )
        val newJob = playerJob(
            id = 1L,
            tags = listOf(
                Tags.CorpseAccess,
                Tags.SocialJob,
            )
        )

        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                availableJobs = listOf(
                    previousJob,
                    newJob,
                ),
            )
        )

        viewModel.handleAction(MainViewAction.DebugJobSelected(id = previousJob.id))
        viewModel.handleAction(MainViewAction.DebugJobSelected(id = newJob.id))

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::drawerState)
                .prop(DrawerViewState::drawerContent)
                .isInstanceOf(DrawerContentViewState.PlayerInfo::class)
                .prop(DrawerContentViewState.PlayerInfo::playerInfo)
                .prop(PlayerInfoState::playerTags)
                .containsExactly(
                    Tags.CorpseAccess,
                    Tags.SocialJob,
                )
        }
    }

    @Test
    fun `should keep other player tags when changing jobs`() = runBlockingTest {
        val playerTags = listOf(
            Tags.Devourer,
            Tags.Immortal,
        )
        val previousJob = playerJob(
            id = 0L,
            tags = listOf(
                Tags.MeatAccess,
                Tags.SocialJob,
            )
        )
        val newJob = playerJob(
            id = 1L,
            tags = listOf(
                Tags.CorpseAccess,
                Tags.SocialJob,
            )
        )
        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                availableJobs = listOf(
                    previousJob,
                    newJob,
                ),
                player = player(
                    tags = playerTags,
                )
            )
        )

        viewModel.handleAction(MainViewAction.DebugJobSelected(id = previousJob.id))
        viewModel.handleAction(MainViewAction.DebugJobSelected(id = newJob.id))

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::drawerState)
                .prop(DrawerViewState::drawerContent)
                .isInstanceOf(DrawerContentViewState.PlayerInfo::class)
                .prop(DrawerContentViewState.PlayerInfo::playerInfo)
                .prop(PlayerInfoState::playerTags)
                .containsExactly(
                    Tags.Devourer,
                    Tags.Immortal,
                    Tags.CorpseAccess,
                    Tags.SocialJob,
                )
        }
    }

    @Test
    fun `should show only actions that require avialable tags`() = runBlockingTest {
        val availableTag = tag(name = "lol")
        val notAvailableTag = tag(name = "kek")

        val availableAction = action(
            id = 1L,
            tags = mapOf(
                availableTag to TagRelation.RequiredAll,
            )
        )
        val notAvailableAction = action(
            id = 2L,
            tags = mapOf(
                availableTag to TagRelation.RequiredAll,
                notAvailableTag to TagRelation.RequiredAll,
            )
        )

        imperativeShell.updateState(
            newState = mainFunctionalCoreState(
                actions = listOf(
                    availableAction,
                    notAvailableAction,
                ),
                player = player(
                    tags = listOf(
                        availableTag
                    )
                )
            )
        )

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::actionState)
                .prop(ActionsState::actionPanes)
                .index(0)
                .prop(ActionPane::actions)
                .extracting(ActionModel::id)
                .containsExactly(availableAction.id)
        }
    }

    private fun createViewModel() = MainViewModel(
        imperativeShell = imperativeShell,
    )

    private fun Assert<MainViewState>.extractingUpgrades() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::shop)
            .prop(ShopState::upgradeLists)
            .index(0)

    private fun Assert<MainViewState>.extractingHumanActions() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::actionPanes)
            .index(0)
            .prop(ActionPane::actions)


    private fun Assert<MainViewState>.extractingRatios() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .extracting(HumanityRatioModel::name, HumanityRatioModel::percent)

    private fun Assert<HumanityRatioModel>.assertPercentage(expected: Double) =
        prop(HumanityRatioModel::percent)
            .isCloseTo(expected, 0.00001)

    private fun Assert<MainViewState>.extractingMutanity() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .index(0)

    private fun Assert<MainViewState>.extractingSuspicion() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .index(1)

    private fun Assert<MainViewState>.extractingMutanityValue() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .index(0)
            .prop(HumanityRatioModel::percent)

    private fun Assert<MainViewState>.extractingMutanityName() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .index(0)
            .prop(HumanityRatioModel::name)

    private fun Assert<MainViewState>.hasRatioName(expectedName: String) = extractingMutanityName()
        .isEqualTo(expectedName)

    private fun Assert<MainViewState>.hasSuspicionRatioName(expectedName: String) =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratios)
            .index(1)
            .prop(HumanityRatioModel::name)
            .isEqualTo(expectedName)

    private fun Assert<MainViewState>.extractingResources() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::resources)

    private fun Assert<MainViewState>.extractingResourceNameAndValues() =
        extractingResources()
            .extracting(ResourceModel::name, ResourceModel::value)

    private fun Assert<MainViewState>.extractingBlood() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::resources)
            .index(0)
            .prop(ResourceModel::value)

    private fun Assert<MainViewState>.extractingMoney() = isInstanceOf(MainViewState.Success::class)
        .prop(MainViewState.Success::resources)
        .index(1)
        .prop(ResourceModel::value)

    private fun Assert<MainViewState>.extractingDebugState() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::drawerState)
            .prop(DrawerViewState::drawerContent)
            .isInstanceOf(DrawerContentViewState.Debug::class)
            .prop(DrawerContentViewState.Debug::state)

    private fun Assert<DebugViewState>.extractingJobSelection() = prop(DebugViewState::jobSelection)

}
