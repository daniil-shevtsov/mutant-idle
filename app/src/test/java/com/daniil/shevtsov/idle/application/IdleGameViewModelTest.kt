package com.daniil.shevtsov.idle.application

import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class IdleGameViewModelTest {
    private val viewModel: IdleGameViewModel by lazy { createViewModel() }

    @Test
    fun kek() {

    }

    private fun createViewModel() = IdleGameViewModel(
        startTime = mockk(),
        observeTime = mockk(),
        updateResources = mockk(),
    )
}