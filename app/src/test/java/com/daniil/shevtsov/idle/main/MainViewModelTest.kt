package com.daniil.shevtsov.idle.main

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.NewResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.data.upgrade.UpgradeStorage
import com.daniil.shevtsov.idle.main.domain.resource.Resource
import com.daniil.shevtsov.idle.main.domain.resource.ResourceSource
import com.daniil.shevtsov.idle.main.domain.upgrade.Upgrade
import com.daniil.shevtsov.idle.main.domain.upgrade.UpgradeStatus
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.shop.ShopState
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeStatusModel
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.clearAllMocks
import kotlinx.coroutines.flow.Flow
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

    private val observeResourceReal: ResourceSource = object : ResourceSource {
        override fun invoke(): Flow<Resource> {
            return resourceBarrier.observeResource()
        }
    }

    private var usedResourceSource: ResourceSource = observeResourceReal

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    private val resourceBarrier = ResourceBehavior(
        balanceConfig = balanceConfig,
        storage = ResourceStorage(),
    )

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
        clearAllMocks()
    }

    private fun useRealSource() {
        usedResourceSource = observeResourceReal
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
        resourceBarrier.updateResource(Time(1000))
        NewResourceBehavior.updateResource(resourceStorage, Time(1000), balanceConfig.resourcePerMillisecond)
        useRealSource()

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))

        assertThat(upgradeStorage.getUpgradeById(id = 1L))
            .isNotNull()
            .prop(Upgrade::status)
            .isEqualTo(UpgradeStatus.Bought)
    }

    @Test
    fun `should mark upgrade as affordable if its price less than resource`() = runBlockingTest {
        resourceBarrier.updateResource(Time(1000))
        NewResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(1000),
            rate = balanceConfig.resourcePerMillisecond,
        )
        useRealSource()

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
            useRealSource()

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
        useRealSource()
        resourceBarrier.updateResource(Time(200))
        NewResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(200),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 2L))
        resourceBarrier.updateResource(Time(201))
        NewResourceBehavior.updateResource(
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
        useRealSource()
        resourceBarrier.updateResource(Time(50))
        NewResourceBehavior.updateResource(
            storage = resourceStorage,
            passedTime = Time(50),
            rate = balanceConfig.resourcePerMillisecond,
        )

        viewModel.handleAction(MainViewAction.UpgradeSelected(id = 1L))
        resourceBarrier.updateResource(Time(1))
        NewResourceBehavior.updateResource(
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