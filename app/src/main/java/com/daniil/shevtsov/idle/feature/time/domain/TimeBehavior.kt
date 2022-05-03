package com.daniil.shevtsov.idle.feature.time.domain

import com.daniil.shevtsov.idle.feature.time.data.TimeStorage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import timber.log.Timber
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
                Timber.d("duration ${duration.inWholeMicroseconds} interval ${interval.inWholeMilliseconds}")
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
