package com.daniil.shevtsov.idle.main.domain.resource

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.util.balanceConfig
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MainCoroutineExtension::class)
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
            resourcePerTick = 2.0,
        )

        val newValue = calculateResourceValue(
            oldValue = 4,
            passedTicks = 5L
        )

        assertThat(newValue)
            .isEqualTo(14L)
    }

    private fun createUseCase() = CalculateResourceValueUseCase(
        balanceConfig = balanceConfig,
    )

}