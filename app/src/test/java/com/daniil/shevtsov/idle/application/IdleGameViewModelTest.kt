package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MainCoroutineExtension::class)
internal class IdleGameViewModelTest {
    private val viewModel: IdleGameViewModel by lazy { createViewModel() }

    private val balanceConfig = BalanceConfig(
        tickRateMillis = 1L,
        resourcePerMillisecond = 2.0,
    )

    private val timeStorage = TimeStorage()

    @Test
    fun kek() = runBlockingTest {
        viewModel.onStart()
    }

    private fun createViewModel() = IdleGameViewModel(
        balanceConfig = balanceConfig,
        startTime = mockk(),
        observeTime = mockk(),
        updateResources = mockk(),
        timeStorage = timeStorage,
    )
}