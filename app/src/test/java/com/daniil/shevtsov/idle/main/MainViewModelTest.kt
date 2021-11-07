package com.daniil.shevtsov.idle.main

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.*
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.main.ui.resource.ResourceModel
import com.daniil.shevtsov.idle.main.ui.upgrade.UpgradeModel
import com.daniil.shevtsov.idle.util.resource
import com.daniil.shevtsov.idle.util.upgrade
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class MainViewModelTest {

    private val observeResource: ObserveResourceUseCase = mockk()
    private val observeUpgrades: ObserveUpgradesUseCase = mockk()

    private val viewModel: MainViewModel by lazy { createViewModel() }

    @BeforeEach
    fun onSetup() {
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

    private fun createViewModel() = MainViewModel(
        observeResource = observeResource,
        observeUpgrades = observeUpgrades,
    )

}