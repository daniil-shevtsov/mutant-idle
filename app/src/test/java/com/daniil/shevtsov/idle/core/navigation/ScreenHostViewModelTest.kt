package com.daniil.shevtsov.idle.core.navigation

import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
class ScreenHostViewModelTest {
    private val viewModel = ScreenHostViewModel(imperativeShell = MainImperativeShell())

    @Test
    fun `should something`() = runBlockingTest {
        viewModel
    }
}
