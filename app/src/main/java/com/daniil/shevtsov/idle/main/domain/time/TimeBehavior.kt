package com.daniil.shevtsov.idle.main.domain.time

import com.daniil.shevtsov.idle.main.data.time.TimeStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import kotlin.time.Duration

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
                timeStorage.setNewValue(Duration.milliseconds(passed))
            }
    }

    fun observeTime(timeStorage: TimeStorage): Flow<Duration> {
        return timeStorage.observeChange()
    }

    private fun timerFlow(interval: Duration): Flow<Duration> = flow {
        var elapsedTime = Duration.milliseconds(0.0)
        while (true) {
            emit(elapsedTime)
            elapsedTime += interval
            delay(interval)
        }
    }

}