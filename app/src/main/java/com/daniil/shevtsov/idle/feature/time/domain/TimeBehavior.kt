package com.daniil.shevtsov.idle.feature.time.domain

import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object TimeBehavior {
    suspend fun startEmitingTime(
        timeStorage: TimeStorage,
        interval: Duration,
        until: Duration,
    ) {
        timerFlow(interval)
            .takeWhile { duration -> duration <= until }
            .collect { duration ->
                val passed = duration.inWholeMilliseconds / interval.inWholeMilliseconds
                timeStorage.setNewValue(passed.milliseconds)
            }
    }

    fun observeTime(timeStorage: TimeStorage): Flow<Duration> {
        return timeStorage.observeChange()
    }

    private fun timerFlow(interval: Duration): Flow<Duration> = flow {
        var elapsedTime = 0.0.milliseconds
        while (true) {
            emit(elapsedTime)
            elapsedTime += interval
            delay(interval)
        }
    }

}
