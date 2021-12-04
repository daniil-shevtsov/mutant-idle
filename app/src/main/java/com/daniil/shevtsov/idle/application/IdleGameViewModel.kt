package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import com.daniil.shevtsov.idle.main.domain.resource.UpdateResourcesUseCase
import com.daniil.shevtsov.idle.main.domain.time.ObserveTimeUseCase
import com.daniil.shevtsov.idle.main.domain.time.StartTimeUseCase
import com.daniil.shevtsov.idle.main.domain.time.TimeBehavior
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration

class IdleGameViewModel @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val startTime: StartTimeUseCase,
    private val observeTime: ObserveTimeUseCase,
    private val updateResources: UpdateResourcesUseCase,
    private val timeStorage: TimeStorage,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun onStart() {
        scope.launch {
            startTime()
            TimeBehavior.startEmitingTime(
                timeStorage = timeStorage,
                interval = Duration.milliseconds(balanceConfig.tickRateMillis),
                until = Duration.INFINITE,
            )
        }
        scope.launch {
            observeTime()
                .onEach { time ->
                    updateResources(time)
                }
                .collect()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

}