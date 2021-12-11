package com.daniil.shevtsov.idle.main.data.time

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.Duration

internal class TimeStorageTest {

    private lateinit var storage: TimeStorage

    @BeforeEach
    fun onSetup() {
        storage = TimeStorage()
    }

    @Test
    fun `should return 0 initially as current value`() = runBlockingTest {
        assertThat(storage.getCurrentValue()).isEqualTo(Duration.ZERO)
    }

    @Test
    fun `should update value correctly`() = runBlockingTest {
        assertThat(storage.getCurrentValue()).isEqualTo(Duration.ZERO)

        storage.setNewValue(Duration.milliseconds(2L))
        assertThat(storage.getCurrentValue()).isEqualTo(Duration.milliseconds(2L))

        storage.setNewValue(Duration.milliseconds(4L))
        assertThat(storage.getCurrentValue()).isEqualTo(Duration.milliseconds(4L))
    }

    @Test
    fun `should get new values to observers`() = runBlockingTest {
        storage.observeChange().test {
            assertThat(awaitItem()).isEqualTo(Duration.ZERO)

            storage.setNewValue(Duration.milliseconds(2L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(2L))

            storage.setNewValue(Duration.milliseconds(4L))
            assertThat(awaitItem()).isEqualTo(Duration.milliseconds(4L))
        }
    }

}