package com.daniil.shevtsov.idle.main

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.BuyUpgradeUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
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

    private val observeResource: ObserveResourceUseCase = mockk()
    private val observeUpgrades: ObserveUpgradesUseCase = mockk()
    private val buyUpgrade: BuyUpgradeUseCase = mockk(relaxUnitFun = true)

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
        clearAllMocks()
        every { observeResource() } returns flowOf(resource(value = 0.0))
        every { observeUpgrades() } returns flowOf(emptyList())
    }

    @Test
    fun `should for correct initial state`() = runBlockingTest {
        every { observeResource() } returns flowOf(resource(value = 2.0))
        every { observeUpgrades() } returns flowOf(listOf(upgrade(id = 1L)))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    prop(MainViewState.Success::resource)
                        .prop(ResourceModel::value)
                        .isEqualTo("2.0")
                    prop(MainViewState.Success::upgrades)
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
        every { observeResource() } returns flowOf(resource(value = 50.0))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::upgrades)
                .extracting(UpgradeModel::status)
                .containsExactly(UpgradeStatusModel.Affordable)
        }
    }

    @Test
    fun `should mark upgrade as not affordable if its price higher than resource`() = runBlockingTest {
        every { observeUpgrades() } returns flowOf(
            listOf(
                upgrade(
                    id = 1L,
                    price = 150.0,
                    status = UpgradeStatus.NotBought
                )
            )
        )
        every { observeResource() } returns flowOf(resource(value = 50.0))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::upgrades)
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
        every { observeResource() } returns flowOf(resource(value = 50.0))

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .prop(MainViewState.Success::upgrades)
                .extracting(UpgradeModel::status)
                .containsExactly(UpgradeStatusModel.Bought)
        }
    }

    private fun createViewModel() = MainViewModel(
        observeResource = observeResource,
        observeUpgrades = observeUpgrades,
        buyUpgrade = buyUpgrade,
    )

}