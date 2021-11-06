package com.daniil.shevtsov.idle.main.domain.resource

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.util.balanceConfig
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CalculateResourceValueUseCaseTest {

    private val calculateResourceValue: CalculateResourceValueUseCase by lazy {
        createUseCase()
    }

    private lateinit var balanceConfig: BalanceConfig

    @BeforeEach
    fun onSetup() {
        balanceConfig = balanceConfig()
    }

    @Test
    fun `should set new resource according to balance config and passed time`() = runBlockingTest {
        balanceConfig = balanceConfig(
            resourcePerMillisecond = 2.0,
        )

        val newValue = calculateResourceValue(
            oldValue = 4.0,
            passedTicks = 5L
        )

        assertThat(newValue)
            .isEqualTo(14.0)
    }

    private fun createUseCase() = CalculateResourceValueUseCase(
        balanceConfig = balanceConfig,
    )

}