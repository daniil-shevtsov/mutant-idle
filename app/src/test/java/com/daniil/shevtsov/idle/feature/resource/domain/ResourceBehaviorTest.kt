package com.daniil.shevtsov.idle.feature.resource.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.resource.data.ResourceStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class ResourceBehaviorTest {

    private val behavior = ResourceBehavior

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    @Test
    fun `should return 0 for resource initially`() = runBlockingTest {
        val storage = ResourceStorage()
        assertThat(behavior.getCurrentResource(storage)).isEqualTo(Resource(0.0))
    }

    @Test
    fun `should update resource by value after tick passed`() = runBlockingTest {
        val storage = ResourceStorage()

        behavior.updateResource(
            storage = storage,
            passedTime = Time(balanceConfig.tickRateMillis),
            rate = balanceConfig.resourcePerMillisecond,
        )
        assertThat(behavior.getCurrentResource(storage)).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

        behavior.updateResource(
            storage = storage,
            passedTime = Time(balanceConfig.tickRateMillis),
            rate = balanceConfig.resourcePerMillisecond,
        )
        assertThat(behavior.getCurrentResource(storage)).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
    }

    @Test
    fun `should got new values of resource to observer`() = runBlockingTest {
        val storage = ResourceStorage()

        behavior.observeResource(storage).test {
            assertThat(awaitItem()).isEqualTo(Resource(0.0))

            behavior.updateResource(
                storage = storage,
                passedTime = Time(balanceConfig.tickRateMillis),
                rate = balanceConfig.resourcePerMillisecond,
            )
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

            behavior.updateResource(
                storage = storage,
                passedTime = Time(balanceConfig.tickRateMillis),
                rate = balanceConfig.resourcePerMillisecond,
            )
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
        }
    }

    @Test
    fun `should decrease resource by value`() = runBlockingTest {
        val storage = ResourceStorage()

        behavior.updateResource(
            storage = storage,
            passedTime = Time(200),
            rate = 1.0,
        )

        behavior.decreaseResource(
            storage = storage,
            amount = 50.0,
        )

        assertThat(behavior.getCurrentResource(storage))
            .isEqualTo(Resource(150.0))
    }

}