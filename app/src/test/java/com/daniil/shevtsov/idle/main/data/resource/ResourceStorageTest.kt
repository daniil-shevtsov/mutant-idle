package com.daniil.shevtsov.idle.main.data.resource

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

}