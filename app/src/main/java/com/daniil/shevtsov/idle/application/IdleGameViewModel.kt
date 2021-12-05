package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.main.data.resource.NewResourceBehavior
import com.daniil.shevtsov.idle.main.data.resource.ResourceStorage
import com.daniil.shevtsov.idle.main.data.time.Time
import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import com.daniil.shevtsov.idle.main.domain.time.TimeBehavior
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration

class IdleGameViewModel @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val timeStorage: TimeStorage,
    private val resourceStorage: ResourceStorage,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    suspend fun onSuspendStart(until: Duration = Duration.INFINITE) {
        startTime(until)
        doEverythingElse()
    }

    fun onStart(until: Duration = Duration.INFINITE) {
        scope.launch {
            startTime(until)
        }
        doEverythingElse()
    }

    private suspend fun startTime(until: Duration) {
        TimeBehavior.startEmitingTime(
            timeStorage = timeStorage,
            interval = Duration.milliseconds(balanceConfig.tickRateMillis),
            until = until,
        )
    }

    private fun doEverythingElse() {
        scope.launch {
            TimeBehavior.observeTime(timeStorage)
                .map { Time(it.inWholeMilliseconds) }
                .onEach { time ->
                    NewResourceBehavior.updateResource(
                        storage = resourceStorage,
                        passedTime = time,
                        rate = balanceConfig.resourcePerMillisecond,
                    )
                }
                .collect()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

}