package com.daniil.shevtsov.idle.feature.resource.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.util.balanceConfig
import com.daniil.shevtsov.idle.util.resource
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

class ResourceBehaviorTest {

    private val behavior = ResourceBehavior

    private val resourcesStorage = ResourcesStorage(
        initialResources = listOf(
            resource(key = ResourceKey.Blood, name = "Blood", value = 0.0)
        )
    )

    private val balanceConfig = balanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    @Test
    fun `should return 0 for resource initially`() = runBlockingTest {
        assertThat(behavior.getCurrentResource(resourcesStorage = resourcesStorage))
            .prop(Resource::value)
            .isEqualTo(0.0)
    }

    @Test
    fun `should update resource by value after tick passed`() = runBlockingTest {
        behavior.updateResource(
            resourcesStorage = resourcesStorage,
            passedTime = Time(balanceConfig.tickRateMillis),
            rate = balanceConfig.resourcePerMillisecond,
        )
        assertThat(
            behavior.getCurrentResource(
                resourcesStorage = resourcesStorage
            )
        )
            .prop(Resource::value)
            .isEqualTo(balanceConfig.resourcePerMillisecond)

        behavior.updateResource(
            resourcesStorage = resourcesStorage,
            passedTime = Time(balanceConfig.tickRateMillis),
            rate = balanceConfig.resourcePerMillisecond,
        )
        assertThat(
            behavior.getCurrentResource(

                resourcesStorage = resourcesStorage
            )
        ).prop(Resource::value)
            .isEqualTo(balanceConfig.resourcePerMillisecond * 2)
    }

    @Test
    fun `should got new values of resource to observer`() = runBlockingTest {
        behavior.observeResource(resourcesStorage = resourcesStorage).test {
            assertThat(awaitItem()).prop(Resource::value)
                .isEqualTo(0.0)

            behavior.updateResource(
                resourcesStorage = resourcesStorage,
                passedTime = Time(balanceConfig.tickRateMillis),
                rate = balanceConfig.resourcePerMillisecond,
            )
            assertThat(awaitItem()).prop(Resource::value)
                .isEqualTo(balanceConfig.resourcePerMillisecond)

            behavior.updateResource(
                resourcesStorage = resourcesStorage,
                passedTime = Time(balanceConfig.tickRateMillis),
                rate = balanceConfig.resourcePerMillisecond,
            )
            assertThat(awaitItem()).prop(Resource::value)
                .isEqualTo(balanceConfig.resourcePerMillisecond * 2)
        }
    }

    @Test
    fun `should decrease resource by value`() = runBlockingTest {
        behavior.updateResource(
            resourcesStorage = resourcesStorage,
            passedTime = Time(200),
            rate = 1.0,
        )

        behavior.decreaseResource(
            resourcesStorage = resourcesStorage,
            amount = 50.0,
        )

        assertThat(
            behavior.getCurrentResource(

                resourcesStorage = resourcesStorage
            )
        ).prop(Resource::value)
            .isEqualTo(150.0)
    }

}