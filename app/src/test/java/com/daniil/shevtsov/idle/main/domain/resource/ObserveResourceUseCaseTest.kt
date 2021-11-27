package com.daniil.shevtsov.idle.main.domain.resource

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.ResourceRepositoryImpl
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.ui.resource.GetCurrentResourceUseCase
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class ObserveResourceUseCaseTest {

    val barrier = Barrier

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    object Barrier {

        private lateinit var observeResourceUseCase: ObserveResourceUseCase
        private lateinit var updateResourceUseCase: UpdateResourceUseCase
        private lateinit var getCurrentResourceUseCase: GetCurrentResourceUseCase

        fun create(balanceConfig: BalanceConfig) {
            val storage = ResourceStorage()
            val repository = ResourceRepositoryImpl(
                storage = storage
            )

            observeResourceUseCase = ObserveResourceUseCase(
                resourceRepository = repository
            )
            updateResourceUseCase = UpdateResourceUseCase(
                balanceConfig = balanceConfig,
                resourceRepository = repository
            )
            getCurrentResourceUseCase = GetCurrentResourceUseCase(
                resourceRepository = repository
            )
        }

        fun observeResource() = observeResourceUseCase()
        suspend fun updateResource(timePassed: Time) = updateResourceUseCase(timePassed)
        suspend fun getCurrentResource() = getCurrentResourceUseCase()

    }

    @BeforeEach
    fun onSetup() {
        barrier.create(balanceConfig)
    }

    @Test
    fun `should return 0 for resource initially`() = runBlockingTest {
        assertThat(barrier.getCurrentResource()).isEqualTo(Resource(0.0))
    }

    @Test
    fun `should update resource by value after tick passed`() = runBlockingTest {
        barrier.updateResource(Time(balanceConfig.tickRateMillis))
        assertThat(barrier.getCurrentResource()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

        barrier.updateResource(Time(balanceConfig.tickRateMillis))
        assertThat(barrier.getCurrentResource()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
    }

    @Test
    fun `should got new values of resource to observer`() = runBlockingTest {

        barrier.observeResource().test {
            assertThat(awaitItem()).isEqualTo(Resource(0.0))

            barrier.updateResource(Time(balanceConfig.tickRateMillis))
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond))

            barrier.updateResource(Time(balanceConfig.tickRateMillis))
            assertThat(awaitItem()).isEqualTo(Resource(balanceConfig.resourcePerMillisecond * 2))
        }
    }

}