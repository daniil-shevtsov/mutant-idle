package com.daniil.shevtsov.idle.main.domain.resource

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.ResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.Time
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class ResourceBehaviorTest {

    private lateinit var behavior: ResourceBehavior

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    @BeforeEach
    fun onSetup() {
        behavior = ResourceBehavior(
            balanceConfig = balanceConfig,
            storage = ResourceStorage(),
        )
    }

    @Test
    fun `should return 0 for resource initially`() = runBlockingTest {
        assertThat(behavior.getCurrentResource()).isEqualTo(Resource(0.0))
    }

    @Test
    fun `should update resource by value after tick passed`() = runBlockingTest {
        behavior.updateResource(Time(balanceConfig.tickRateMillis))
        assertThat(behavior.getCurrentResource()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

        behavior.updateResource(Time(balanceConfig.tickRateMillis))
        assertThat(behavior.getCurrentResource()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
    }

    @Test
    fun `should got new values of resource to observer`() = runBlockingTest {
        behavior.observeResource().test {
            assertThat(awaitItem()).isEqualTo(Resource(0.0))

            behavior.updateResource(Time(balanceConfig.tickRateMillis))
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

            behavior.updateResource(Time(balanceConfig.tickRateMillis))
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
        }
    }

}