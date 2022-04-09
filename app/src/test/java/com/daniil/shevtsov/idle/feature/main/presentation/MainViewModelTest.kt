package com.daniil.shevtsov.idle.feature.main.presentation

import app.cash.turbine.test
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.action.data.ActionsStorage
import com.daniil.shevtsov.idle.feature.action.domain.ActionType
import com.daniil.shevtsov.idle.feature.action.presentation.ActionModel
import com.daniil.shevtsov.idle.feature.action.presentation.ActionPane
import com.daniil.shevtsov.idle.feature.action.presentation.ActionsState
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.data.RatiosStorage
import com.daniil.shevtsov.idle.feature.ratio.domain.Ratio
import com.daniil.shevtsov.idle.feature.ratio.domain.RatioKey
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.Resource
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import com.daniil.shevtsov.idle.util.action
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class MainViewModelTest {

    private var upgradeStorage = UpgradeStorage(
        initialUpgrades = emptyList()
    )
    private var actionsStorage = ActionsStorage(
        initialActions = emptyList()
    )

    private val resourcesStorage = ResourcesStorage(
        initialResources = listOf(
            Resource(key = ResourceKey.Blood, name = "Blood", value = 0.0),
            Resource(key = ResourceKey.Money, name = "Money", value = 0.0),
        )
    )
    private val mutantRatioStorage = MutantRatioStorage()
    private val ratiosStorage = RatiosStorage(
        initialRatios = listOf(
            Ratio(key = RatioKey.Mutanity, title = "", value = 0.0),
            Ratio(key = RatioKey.Suspicion, title = "", value = 0.0),
        )
    )

    private val resourceSpentForFullMutant = 100.0
    private val balanceConfig = balanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
        resourceSpentForFullMutant = resourceSpentForFullMutant,
    )

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
        clearAllMocks()
    }

    @Test
    fun `should form correct initial state`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 0L),
                upgrade(id = 1L, price = 25.0),
                upgrade(id = 2L, price = 150.0),
                upgrade(id = 3L, price = 10.0),
            )
        )
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(id = 0L, title = "human action", actionType = ActionType.Human),
                action(id = 1L, title = "mutant action", actionType = ActionType.Mutant),
            )
        )

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    extractingResources()
                        .containsExactly("Blood" to "0", "Money" to "0")
                    extractingRatios()
                        .containsExactly("Human" to 0.0, "Unknown" to 0.0)
                    prop(MainViewState.Success::shop)
                        .prop(ShopState::upgradeLists)
                        .index(0)
                        .extracting(UpgradeModel::id)
                        .containsExactly(0L, 1L, 2L, 3L)
                    prop(MainViewState.Success::actionState)
                        .prop(ActionsState::humanActionPane)
                        .prop(ActionPane::actions)
                        .extracting(ActionModel::title)
                        .containsExactly("human action")
                    prop(MainViewState.Success::actionState)
                        .prop(ActionsState::mutantActionPane)
                        .prop(ActionPane::actions)
                        .extracting(ActionModel::title)
                        .containsExactly("mutant action")
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
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        setInitialResourceValue(10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 5.0)))

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        assertThat(upgradeStorage.getUpgradeById(id = 0L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        setInitialResourceValue(10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 5.0)))

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
            setInitialResourceValue(10.0)
            upgradeStorage =
                UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 20.0)))

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
        setInitialResourceValue(10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 10.0)))

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
        setInitialResourceValue(10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 10.0)))

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .extractingMutanityValue()
                .isEqualTo(0.10)
        }
    }

    private fun setInitialResourceValue(
        value: Double,
        key: ResourceKey = ResourceKey.Blood
    ) {
        val resource = resourcesStorage.getByKey(key = key)
        resourcesStorage.updateByKey(
            key = key,
            newValue = resource!!.copy(value = value)
        )
    }

    @Test
    fun `should update mutant ratio names correctly`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 0L, price = 15.0),
                upgrade(id = 1L, price = 10.0),
                upgrade(id = 2L, price = 25.0),
                upgrade(id = 3L, price = 30.0),
            )
        )
        setInitialResourceValue(value = 1000.0)

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
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(id = 0L, ratioChanges = mapOf(RatioKey.Suspicion to 0.15f)),
                action(id = 1L, ratioChanges = mapOf(RatioKey.Suspicion to 0.10f)),
                action(id = 2L, ratioChanges = mapOf(RatioKey.Suspicion to 0.25f)),
                action(id = 3L, ratioChanges = mapOf(RatioKey.Suspicion to 0.30f)),
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
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(id = 0L, ratioChanges = mapOf(RatioKey.Suspicion to 0.15f)),
                action(id = 1L, ratioChanges = mapOf(RatioKey.Mutanity to 0.25f)),
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
        setInitialResourceValue(value = 1000.0)
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(ResourceKey.Blood to 50.0)
                )
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
            setInitialResourceValue(key = ResourceKey.Blood, value = 1000.0)
            setInitialResourceValue(key = ResourceKey.Money, value = 500.0)
            actionsStorage = ActionsStorage(
                initialActions = listOf(
                    action(
                        id = 1L,
                        resourceChanges = mapOf(
                            ResourceKey.Blood to 50.0,
                            ResourceKey.Money to -30.0,
                        ),
                    )
                )
            )

            viewModel.state.test {
                assertThat(expectMostRecentItem())
                    .extractingResources()
                    .containsExactly("Blood" to "1000", "Money" to "500")

                viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

                assertThat(expectMostRecentItem())
                    .extractingResources()
                    .containsExactly("Blood" to "1050", "Money" to "470")
            }
        }

    @Test
    fun `should toggle corresponding collapse state when toggle clicked`() = runBlockingTest {
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
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(ResourceKey.Money to -30.0),
                )
            )
        )
        setInitialResourceValue(key = ResourceKey.Money, value = 10.0)

        viewModel.state.test {
            viewModel.handleAction(MainViewAction.ActionClicked(id = 1L))

            assertThat(expectMostRecentItem())
                .extractingMoney()
                .isEqualTo("10")
        }
    }

    @Test
    fun `should not apply action if it requires both available and not available resources`() =
        runBlockingTest {
            actionsStorage = ActionsStorage(
                initialActions = listOf(
                    action(
                        id = 1L,
                        resourceChanges = mapOf(
                            ResourceKey.Money to -30.0,
                            ResourceKey.Blood to -50.0,
                        ),
                    )
                )
            )
            setInitialResourceValue(key = ResourceKey.Money, value = 40.0)
            setInitialResourceValue(key = ResourceKey.Blood, value = 30.0)

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
    fun `should show actions inactive if it requires not available resources`() = runBlockingTest {
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -30.0,
                    ),
                    actionType = ActionType.Human,
                ),
                action(
                    id = 2L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -50.0,
                    ),
                    actionType = ActionType.Human,
                )
            )
        )
        setInitialResourceValue(key = ResourceKey.Money, value = 35.0)

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingHumanActions()
                .extracting(ActionModel::id, ActionModel::isActive)
                .containsExactly(
                    1L to true,
                    2L to false,
                )
        }
    }

    @Test
    fun `show active actions before inactive if got both`() = runBlockingTest {
        actionsStorage = ActionsStorage(
            initialActions = listOf(
                action(
                    id = 1L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -60.0,
                    ),
                    actionType = ActionType.Human,
                ),
                action(
                    id = 2L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -10.0,
                    ),
                    actionType = ActionType.Human,
                ),
                action(
                    id = 3L,
                    resourceChanges = mapOf(
                        ResourceKey.Money to -50.0,
                    ),
                    actionType = ActionType.Human,
                ),
            )
        )
        setInitialResourceValue(key = ResourceKey.Money, value = 35.0)

        viewModel.state.test {
            assertThat(expectMostRecentItem())
                .extractingHumanActions()
                .extracting(ActionModel::id)
                .containsExactly(2L, 1L, 3L)
        }
    }

    private fun createViewModel() = MainViewModel(
        balanceConfig = balanceConfig,
        upgradeStorage = upgradeStorage,
        actionsStorage = actionsStorage,
        resourcesStorage = resourcesStorage,
        mutantRatioStorage = mutantRatioStorage,
        ratiosStorage = ratiosStorage,
    )

    private fun Assert<MainViewState>.extractingUpgrades() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::shop)
            .prop(ShopState::upgradeLists)
            .index(0)

    private fun Assert<MainViewState>.extractingHumanActions() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::actionState)
            .prop(ActionsState::humanActionPane)
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
}