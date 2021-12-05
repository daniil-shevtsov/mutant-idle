package com.daniil.shevtsov.idle.main.domain.time

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import kotlin.time.Duration

internal class TimeBehaviorTest {

    private val behavior = TimeBehavior

    @Test
    fun `should emit time until given`() = runBlockingTest {
        val timeStorage = TimeStorage()
        val interval = Duration.milliseconds(1L)

        behavior.observeTime(timeStorage).test {
            behavior.startEmitingTime(timeStorage, interval, Duration.milliseconds(4L))

            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(0L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(1L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(2L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(3L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(4L))
        }
    }

}