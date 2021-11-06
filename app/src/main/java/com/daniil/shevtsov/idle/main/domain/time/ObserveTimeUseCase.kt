package com.daniil.shevtsov.idle.main.domain.time

import com.daniil.shevtsov.idle.main.data.time.TimeProvider
import javax.inject.Inject

class ObserveTimeUseCase @Inject constructor(
    private val timeProvider: TimeProvider,
) {
    operator fun invoke() = timeProvider.observeTime()
}