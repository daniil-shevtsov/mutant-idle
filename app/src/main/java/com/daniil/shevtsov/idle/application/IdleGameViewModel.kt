package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.resource.data.ResourcesStorage
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceBehavior
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.feature.time.domain.TimeBehavior
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration

class IdleGameViewModel @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val timeStorage: TimeStorage,
    private val resourcesStorage: ResourcesStorage,
    private val imperativeShell: MainImperativeShell,
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
                    ResourceBehavior.updateResourceByTime(
                        resourcesStorage = resourcesStorage,
                        resourceKey = ResourceKey.Blood,
                        passedTime = time,
                        rate = balanceConfig.resourcePerMillisecond,
                    )

                    val currentState = imperativeShell.getState()
                    imperativeShell.updateState(
                        newState = currentState.copy(
                            resources = currentState.resources.map { resource ->
                                when (resource.key) {
                                    ResourceKey.Blood -> resource.copy(
                                        value = time.value * balanceConfig.resourcePerMillisecond
                                    )
                                    else -> resource
                                }
                            }
                        )
                    )
                }
                .collect()
        }
    }

    fun onCleared() {
        scope.cancel()
    }

}
