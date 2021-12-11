package com.daniil.shevtsov.idle.feature.main.presentation

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.ratio.presentation.HumanityRatioModel
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.resource.presentation.ResourceModel
import com.daniil.shevtsov.idle.feature.shop.presentation.ShopState
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.feature.upgrade.data.UpgradeStorage
import com.daniil.shevtsov.idle.feature.upgrade.domain.Upgrade
import com.daniil.shevtsov.idle.feature.upgrade.domain.UpgradeStatus
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeModel
import com.daniil.shevtsov.idle.feature.upgrade.presentation.UpgradeStatusModel
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.clearAllMocks
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class MainViewModelTest {

    private val upgradeStorage = UpgradeStorage(
        initialUpgrades = listOf(
            upgrade(id = 0L),
            upgrade(id = 1L, price = 25.0),
            upgrade(id = 2L, price = 150.0),
            upgrade(id = 3L, price = 10.0),
        )
    )
    private val resourceStorage = ResourceStorage()

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
        clearAllMocks()
    }

    @Test
    fun `should for correct initial state`() = runBlockingTest {
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
        ResourceBehavior.updateResource(resourceStorage, Time(1000), balanceConfig.resourcePerMillisecond)

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))

        assertThat(upgradeStorage.getUpgradeById(id = 1L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        ResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(1000),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::shop)
                .prop(ShopState::upgradeLists)
                .index(0)
                .any {
                    it.prop(UpgradeModel::id).isEqualTo(1L)
                    it.prop(UpgradeModel::status).isEqualTo(UpgradeStatusModel.Affordable)
                }
        }
    }

    @Test
    fun `should mark upgrade as not affordable if its price higher than resource`() =
        runBlockingTest {
            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state)
                    .isInstanceOf(MainViewState.Success::class)
                    .prop(MainViewState.Success::shop)
                    .prop(ShopState::upgradeLists)
                    .index(0)
                    .any {
                        it.prop(UpgradeModel::id).isEqualTo(2L)
                        it.prop(UpgradeModel::status).isEqualTo(UpgradeStatusModel.NotAffordable)
                    }
            }
        }

    @Test
    fun `should mark upgrade as bought if it is bought`() = runBlockingTest {
        ResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(200),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 2L))

        ResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(201),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::shop)
                .prop(ShopState::upgradeLists)
                .index(0)
                .any {
                    it.prop(UpgradeModel::id).isEqualTo(2L)
                    it.prop(UpgradeModel::status).isEqualTo(UpgradeStatusModel.Bought)
                }
        }
    }

    @Test
    fun `should sort upgrades by status`() = runBlockingTest {
        ResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(50),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))

        ResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(1),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::shop)
                .prop(ShopState::upgradeLists)
                .index(0)
                .extracting(UpgradeModel::status)
                .containsExactly(
                    UpgradeStatusModel.Affordable,
                    UpgradeStatusModel.Affordable,
                    UpgradeStatusModel.NotAffordable,
                    UpgradeStatusModel.Bought,
                )
        }
    }

    private fun createViewModel() = MainViewModel(
        upgradeStorage = upgradeStorage,
        resourceStorage = resourceStorage,
    )

}