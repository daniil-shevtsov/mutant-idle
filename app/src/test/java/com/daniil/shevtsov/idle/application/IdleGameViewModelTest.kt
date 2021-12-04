package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class IdleGameViewModelTest {
    private val viewModel: IdleGameViewModel by lazy { createViewModel() }

    private val timeStorage = TimeStorage()

    @Test
    fun kek() {

    }

    private fun createViewModel() = IdleGameViewModel(
        startTime = mockk(),
        observeTime = mockk(),
        updateResources = mockk(),
        timeStorage = timeStorage,
    )
}