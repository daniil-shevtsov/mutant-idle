package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.MainCoroutineExtension
import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
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
    private val resourceStorage = ResourceStorage()

    @Test
    fun kek() = runBlockingTest {
        viewModel.onStart()

        viewModel.onCleared()
    }

    private fun createViewModel() = IdleGameViewModel(
        balanceConfig = balanceConfig,
        timeStorage = timeStorage,
        resourceStorage = resourceStorage,
    )
}