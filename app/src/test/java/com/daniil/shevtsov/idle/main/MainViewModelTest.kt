package com.daniil.shevtsov.idle.main

import app.cash.turbine.test
import assertk.all
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.prop
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.main.domain.resource.ObserveResourceUseCase
import com.daniil.shevtsov.idle.main.domain.upgrade.ObserveUpgradesUseCase
import com.daniil.shevtsov.idle.main.ui.MainViewState
import com.daniil.shevtsov.idle.util.resource
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
    fun `lol - kek`() = runBlockingTest {

        viewModel.state.test {
            val state = expectMostRecentItem()
            assertThat(state)
                .isInstanceOf(MainViewState.Success::class)
                .all {
                    prop(MainViewState.Success::resource).isNotNull()
                    prop(MainViewState.Success::upgrades).isEmpty()
                }
        }
    }

    private fun createViewModel() = MainViewModel(
        observeResource = observeResource,
        observeUpgrades = observeUpgrades,
    )

}