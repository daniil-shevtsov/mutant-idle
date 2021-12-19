package com.daniil.shevtsov.idle.feature.main.presentation

import app.cash.turbine.test
import assertk.Assert
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.ratio.data.MutantRatioStorage
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
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
    private val resourceStorage = ResourceStorage()
    private val mutantRatioStorage = MutantRatioStorage()

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

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    prop(MainViewState.Success::resources)
                        .extracting(ResourceModel::value)
                        .containsExactly("0")
                    prop(MainViewState.Success::ratio)
                        .all {
                            prop(HumanityRatioModel::name).isEqualTo("Human")
                            prop(HumanityRatioModel::percent).isEqualTo(0.0)
                        }
                    prop(MainViewState.Success::shop)
                        .prop(ShopState::upgradeLists)
                        .index(0)
                        .extracting(UpgradeModel::id)
                        .containsExactly(0L, 1L, 2L, 3L)
                }
        }
    }

    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        resourceStorage.setNewValue(resource = 10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 5.0)))

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        assertThat(upgradeStorage.getUpgradeById(id = 0L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        resourceStorage.setNewValue(resource = 10.0)
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
            resourceStorage.setNewValue(resource = 10.0)
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
        resourceStorage.setNewValue(resource = 10.0)
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
        resourceStorage.setNewValue(resource = 10.0)
        upgradeStorage = UpgradeStorage(initialUpgrades = listOf(upgrade(id = 0L, price = 10.0)))

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 0L))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::ratio)
                .prop(HumanityRatioModel::percent)
                .isEqualTo(0.10)
        }
    }

    @Test
    fun `should update ratio names correctly`() = runBlockingTest {
        upgradeStorage = UpgradeStorage(
            initialUpgrades = listOf(
                upgrade(id = 0L, price = 15.0),
                upgrade(id = 1L, price = 10.0),
                upgrade(id = 2L, price = 25.0),
                upgrade(id = 3L, price = 30.0),
            )
        )
        resourceStorage.setNewValue(resource = 1000.0)

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

    private fun createViewModel() = MainViewModel(
        balanceConfig = balanceConfig,
        upgradeStorage = upgradeStorage,
        resourceStorage = resourceStorage,
        mutantRatioStorage = mutantRatioStorage,
    )

    private fun Assert<MainViewState>.extractingUpgrades() =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::shop)
            .prop(ShopState::upgradeLists)
            .index(0)

    private fun Assert<MainViewState>.hasRatioName(expectedName: String) =
        isInstanceOf(MainViewState.Success::class)
            .prop(MainViewState.Success::ratio)
            .prop(HumanityRatioModel::name)
            .isEqualTo(expectedName)

}