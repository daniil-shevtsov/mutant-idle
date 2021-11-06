package com.daniil.shevtsov.idle.main.data.time

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import kotlin.time.Duration

class TimeProviderImpl @Inject constructor() : TimeProvider {

    private val passingTime = MutableStateFlow(Duration.ZERO)

    override suspend fun startUpdatingItemWith(
        interval: Duration
    ) {
        timerFlow(interval)
            .onEach { duration ->
                val passed = duration.inWholeMilliseconds / interval.inWholeMilliseconds
                passingTime.emit(Duration.milliseconds(passed))
            }
            .collect()
    }

    override fun observeTime(): Flow<Time> {
        return passingTime.asStateFlow()
            .map { duration -> TimeDto(passedInterval = duration) }
            .map { dto -> Time(value = dto.passedInterval.inWholeMilliseconds) }
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