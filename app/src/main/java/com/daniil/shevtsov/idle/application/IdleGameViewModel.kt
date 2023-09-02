package com.daniil.shevtsov.idle.application

import com.daniil.shevtsov.idle.core.BalanceConfig
import com.daniil.shevtsov.idle.feature.main.data.MainImperativeShell
import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import com.daniil.shevtsov.idle.feature.time.domain.TimeBehavior
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

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
            interval = balanceConfig.tickRateMillis.milliseconds,
            until = until,
        )
    }

    private fun doEverythingElse() {
//        scope.launch {
//            timeStorage.observeChange()
//                .map { it.inWholeMilliseconds }
//                .scan(0L to 0L) { previousPair, newTime -> previousPair.second to newTime }
//                .map { (previous, new) ->
//                    val difference = new - previous
//                    Time(difference)
//                }
//                .onEach { time ->
//                    val currentState = imperativeShell.getState()
//
//                    val oldResourceValue =
//                        currentState.resources.find { it.key == ResourceKey.Blood }!!.value
//                    val resourceChange =
//                        oldResourceValue + time.value * balanceConfig.resourcePerMillisecond
//                    imperativeShell.updateState(
//                        newState = currentState.copy(
//                            resources = currentState.resources.map { resource ->
//                                when (resource.key) {
////                                    ResourceKey.Blood -> resource.copy(
////                                        value = resourceChange
////                                    )
//                                    else -> resource
//                                }
//                            }
//                        )
//                    )
//                }
//                .collect()
//        }
    }

    fun onCleared() {
        scope.cancel()
    }

}
