package com.daniil.shevtsov.idle.application

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.time.Duration

@ExtendWith(MainCoroutineExtension::class)
internal class IdleGameViewModelTest {
    private val viewModel: IdleGameViewModel by lazy { createViewModel() }

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    private val timeStorage = TimeStorage()
    private val resourceStorage = ResourceStorage()

    @Test
    fun `temporary e2e test`() = runBlockingTest {
        viewModel.onStart()

        viewModel.onCleared()
    }

    @Test
    @Disabled("Some day I will understand coroutines well enough")
    fun `should update time after game started`() = runBlocking {
        timeStorage.observeChange().test {
            viewModel.onStart(until = Duration.milliseconds(4L))

            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(0L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(1L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(2L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(3L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(4L))

            viewModel.onCleared()
        }
    }

    @Test
    fun `should update time after game started suspend`() = runBlockingTest {
        timeStorage.observeChange().test {
            viewModel.onSuspendStart(until = Duration.milliseconds(4L))

            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(0L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(1L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(2L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(3L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(4L))

            viewModel.onCleared()
        }
    }

    private fun createViewModel() = IdleGameViewModel(
        balanceConfig = balanceConfig,
        timeStorage = timeStorage,
        resourceStorage = resourceStorage,
    )
}