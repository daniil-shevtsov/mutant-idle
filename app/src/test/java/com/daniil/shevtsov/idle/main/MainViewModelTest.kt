package com.daniil.shevtsov.idle.main

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCaseTest
import com.daniil.shevtsov.idle.main.domain.upgrade.BuyUpgradeUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.shop.ShopState
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeStatusModel
import com.daniil.shevtsov.idle.util.resource
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.clearAllMocks
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class MainViewModelTest {

    private val observeResourceMock: ObserveResourceUseCase = mockk()
    private val resourceBarrier = ObserveResourceUseCaseTest.Barrier
    private val observeUpgrades: ObserveUpgradesUseCase = mockk()
    private val buyUpgrade: BuyUpgradeUseCase = mockk(relaxUnitFun = true)

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
        clearAllMocks()
        every { observeResourceMock() } returns flowOf(resource(value = 0.0))
        every { observeUpgrades() } returns flowOf(emptyList())

        resourceBarrier.create(balanceConfig)
    }

    @Test
    fun `should for correct initial state`() = runBlockingTest {
        every { observeResourceMock() } returns flowOf(resource(value = 2.0))
        every { observeUpgrades() } returns flowOf(listOf(upgrade(id = 1L)))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    prop(MainViewState.Success::resources)
                        .extracting(ResourceModel::value)
                        .containsExactly("2")
                    prop(MainViewState.Success::shop)
                        .prop(ShopState::upgradeLists)
                        .index(0)
                        .extracting(UpgradeModel::id)
                        .containsExactly(1L)
                }
        }
    }

    @Test
    fun `should buy upgrade when clicked and affordable`() = runBlockingTest {
        every { observeUpgrades() } returns flowOf(listOf(upgrade(id = 1L)))

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))

        coVerify { buyUpgrade(id = 1L) }
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        every { observeUpgrades() } returns flowOf(
            listOf(
                upgrade(
                    id = 1L,
                    price = 25.0,
                    status = UpgradeStatus.NotBought
                )
            )
        )
        every { observeResourceMock() } returns flowOf(resource(value = 50.0))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::shop)
                .prop(ShopState::upgradeLists)
                .index(0)
                .extracting(UpgradeModel::status)
                .containsExactly(UpgradeStatusModel.Affordable)
        }
    }

    @Test
    fun `should mark upgrade as not affordable if its price higher than resource`() =
        runBlockingTest {
            every { observeUpgrades() } returns flowOf(
                listOf(
                    upgrade(
                        id = 1L,
                        price = 150.0,
                        status = UpgradeStatus.NotBought
                    )
                )
            )
            every { observeResourceMock() } returns flowOf(resource(value = 50.0))

            viewModel.state.test {
                val state = expectMostRecentItem()
                assertThat(state)
                    .isInstanceOf(MainViewState.Success::class)
                    .prop(MainViewState.Success::shop)
                    .prop(ShopState::upgradeLists)
                    .index(0)
                    .extracting(UpgradeModel::status)
                    .containsExactly(UpgradeStatusModel.NotAffordable)
            }
        }

    @Test
    fun `should mark upgrade as bought if it is bought`() = runBlockingTest {
        every { observeUpgrades() } returns flowOf(
            listOf(
                upgrade(
                    id = 1L,
                    price = 150.0,
                    status = UpgradeStatus.Bought
                )
            )
        )
        every { observeResourceMock() } returns flowOf(resource(value = 50.0))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::shop)
                .prop(ShopState::upgradeLists)
                .index(0)
                .extracting(UpgradeModel::status)
                .containsExactly(UpgradeStatusModel.Bought)
        }
    }

    @Test
    fun `should sort upgrades by status`() = runBlockingTest {
        every { observeUpgrades() } returns flowOf(
            listOf(
                upgrade(id = 1L, price = 150.0, status = UpgradeStatus.Bought),
                upgrade(id = 2L, price = 150.0, status = UpgradeStatus.NotBought),
                upgrade(id = 3L, price = 25.0, status = UpgradeStatus.NotBought),
            )
        )
        every { observeResourceMock() } returns flowOf(resource(value = 50.0))

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
                    UpgradeStatusModel.NotAffordable,
                    UpgradeStatusModel.Bought,
                )
        }
    }

    private fun createViewModel() = MainViewModel(
        observeResource = observeResourceMock,
        observeUpgrades = observeUpgrades,
        buyUpgrade = buyUpgrade,
    )

}