package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.resource.domain.ResourceKey
import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import com.daniil.shevtsov.idle.feature.time.domain.Time
import com.daniil.shevtsov.idle.feature.time.domain.TimeBehavior
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.scan
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration

class IdleGameViewModel @Inject constructor(
    private val balanceConfig: BalanceConfig,
    private val timeStorage: TimeStorage,
    private val imperativeShell: MainImperativeShell,
) {
    private val scope = CoroutineScope(Job() + Dispatchers.Main.immediate)

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
            timeStorage.observeChange()
                .map { it.inWholeMilliseconds }
                .scan(0L to 0L) { previousPair, newTime -> previousPair.second to newTime }
                .map { (previous, new) ->
                    val difference = new - previous
                    Timber.d("previous $previous new $new")
                    Time(difference)
                }
                .onEach { time ->
                    val currentState = imperativeShell.getState()

                    val oldResourceValue =
                        currentState.resources.find { it.key == ResourceKey.Blood }!!.value
                    val resourceChange =
                        oldResourceValue + time.value * balanceConfig.resourcePerMillisecond
                    Timber.d("time: $time oldResourceValue: $oldResourceValue balanceCOnfig: ${balanceConfig.resourcePerMillisecond} resource change: $resourceChange")
                    imperativeShell.updateState(
                        newState = currentState.copy(
                            resources = currentState.resources.map { resource ->
                                when (resource.key) {
                                    ResourceKey.Blood -> resource.copy(
                                        value = resourceChange
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
