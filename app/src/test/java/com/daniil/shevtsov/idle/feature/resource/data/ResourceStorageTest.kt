package com.daniil.shevtsov.idle.feature.resource.data

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.daniil.shevtsov.idle.MainCoroutineExtension
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class ResourceStorageTest {

    private lateinit var storage: ResourceStorage

    @BeforeEach
    fun onSetup() {
        storage = ResourceStorage()
    }

    @Test
    fun `should return 0 initially as current value`() = runBlockingTest {
        assertThat(storage.getCurrentValue()).isEqualTo(0.0)
    }

    @Test
    fun `should update value correctly`() = runBlockingTest {
        assertThat(storage.getCurrentValue()).isEqualTo(0.0)

        storage.setNewValue(2.0)
        assertThat(storage.getCurrentValue()).isEqualTo(2.0)

        storage.setNewValue(4.0)
        assertThat(storage.getCurrentValue()).isEqualTo(4.0)
    }

    @Test
    fun `should get new values to observers`() = runBlockingTest {
        storage.observeChange().test {
            assertThat(awaitItem()).isEqualTo(0.0)

            storage.setNewValue(2.0)
            assertThat(awaitItem()).isEqualTo(2.0)

            storage.setNewValue(4.0)
            assertThat(awaitItem()).isEqualTo(4.0)
        }
    }

}